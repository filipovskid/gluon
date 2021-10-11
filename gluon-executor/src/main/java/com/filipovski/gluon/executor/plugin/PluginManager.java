package com.filipovski.gluon.executor.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * {@link PluginManager} is responsible for loading plugins and keeping track of their
 * loaders. Plugin loading is conducted using separate {@link PluginLoader}s, enabling
 * dependency isolation through independent {@link PluginClassLoader}s.
 *
 * <p>{@link PluginManager#load} methods are used for loading service provider interface(SPI)
 * implementations from the plugins.</p>
 */

public class PluginManager {

    private final Logger logger = LogManager.getLogger(PluginManager.class);

    private final PluginFinder pluginFinder;

    private final ConcurrentMap<String, PluginLoader> pluginLoaders;

    private List<PluginDescriptor> pluginDescriptors;

    private final ClassLoader parentClassLoader;

    private boolean initialized;

    public PluginManager(PluginFinder pluginFinder) {
        this(pluginFinder, PluginManager.class.getClassLoader());
    }

    public PluginManager(PluginFinder pluginFinder, ClassLoader parentClassLoader) {
        this.pluginFinder = pluginFinder;
        this.pluginLoaders = new ConcurrentHashMap<>();
        this.parentClassLoader = parentClassLoader;
        this.initialized = false;
    }

    public void initialize() {
        if (initialized) {
            logger.warn("Plugin manager has already been initialized!");
            return;
        }

        initialized = true;
        pluginDescriptors = pluginFinder.findPlugins();
    }

    private void assertInitialized() {
        if (!initialized)
            throw new IllegalStateException("Plugin manager has not been initialized!");
    }

    public List<PluginDescriptor> getPluginDescriptors() {
        return pluginDescriptors;
    }

    /**
     * Returns a collection of service providers, which implement the supplied SPI, for a
     * given plugin descriptor.
     *
     * @param pluginDescriptor descriptor of a plugin for which implementations are loaded.
     * @param service service provider interface for which implementations are loaded from the plugin.
     * @param <S> type of the service interface.
     *
     * @return Collection of service implementations within a plugin for the given service.
     */
    public <S> Collection<S> load(PluginDescriptor pluginDescriptor, Class<S> service) {
        assertInitialized();

        PluginLoader pluginLoader = pluginLoaders.computeIfAbsent(
                pluginDescriptor.getPluginId(),
                pluginId -> PluginLoader.create(pluginDescriptor, parentClassLoader)
        );

        return pluginLoader.load(service);
    }

    /**
     * Returns a specific service interface implementation for a given plugin descriptor.
     *
     * @param pluginDescriptor descriptor of a plugin for which implementations are loaded.
     * @param service service provider interface for which implementations are loaded from the plugin.
     * @param className fully-qualified name of a class which implements the service interface.
     * @param <S> type of the service interface.
     *
     * @return Implementation of the service interface whose class name mathes the supplied className
     *         parameter. If the class does not exist, an empty optional is returned.
     */
    public <S> Optional<S> load(PluginDescriptor pluginDescriptor, Class<S> service, String className) {
        assertInitialized();

        PluginLoader pluginLoader = pluginLoaders.computeIfAbsent(
                pluginDescriptor.getPluginId(),
                pluginId -> PluginLoader.create(pluginDescriptor, parentClassLoader)
        );

        return pluginLoader.load(service, className);
    }

    /**
     * Returns a collection of service providers, which implement the supplied SPI, for all
     * plugin descriptors found by the manager.
     *
     * @param service service provider interface for which implementations are loaded from the plugin.
     * @param <S> type of the service interface.
     *
     * @return Collection of service implementations within known plugins.
     */
    public <S> Collection<S> loadAll(Class<S> service) {
        assertInitialized();

        return pluginDescriptors.stream()
                .flatMap(pluginDescriptor -> load(pluginDescriptor, service).stream())
                .collect(Collectors.toList());
    }

}
