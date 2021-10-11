package com.filipovski.gluon.executor.plugin;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import com.filipovski.gluon.executor.util.ContextClassLoaderManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * {@link PluginLoader} provides an environment for loading a single plugin. It loads
 * service providers using the {@link PluginClassLoader} and the  loading facility
 * implemented by {@link ServiceLoader}. This mechanism allows for locating and loading
 * service providers available within the plugin given a Service Provider Interface (SPI).
 */

public class PluginLoader implements AutoCloseable {

    private final Logger logger = LogManager.getLogger(PluginLoader.class);

    private final String pluginId;

    private final URLClassLoader pluginClassLoader;

    private PluginLoader(String pluginId, URLClassLoader pluginClassLoader) {
        this.pluginId = pluginId;
        this.pluginClassLoader = pluginClassLoader;
    }

    public static PluginLoader create(PluginDescriptor pluginDescriptor, ClassLoader parentClassLoader) {
        return new PluginLoader(
                pluginDescriptor.getPluginId(),
                createPluginClassLoader(pluginDescriptor, parentClassLoader)
        );
    }

    private static PluginClassLoader createPluginClassLoader(PluginDescriptor pluginDescriptor,
                                                             ClassLoader parentClassLoader) {
        return new PluginClassLoader(pluginDescriptor.getPluginResourceURLs(), parentClassLoader, true);
    }

    public <S> Collection<S> load(Class<S> service) {
        try (ContextClassLoaderManager loaderManager = ContextClassLoaderManager.with(pluginClassLoader)) {
            return ServiceLoader.load(service, pluginClassLoader)
                    .stream()
                    .map(ServiceLoader.Provider::get)
                    .collect(Collectors.toList());
        }
    }

    public <S> Optional<S> load(Class<S> service, String className) {
        return load(service).stream()
                .filter(s -> s.getClass().getName().equals(className))
                .findFirst();
    }

    @Override
    public void close() {
        try {
            pluginClassLoader.close();
        } catch (IOException e) {
            logger.warn("An exception occurred while closing the classloader for plugin [{}]. [{}]", pluginId, e);
        }
    }
}
