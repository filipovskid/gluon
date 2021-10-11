package com.filipovski.gluon.executor.plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A custom class loader for loading plugins in isolation, unless specified otherwise. Plugin
 * class loader is thread-safe and supports concurrent loading of classes.
 *
 * <p>{@link PluginClassLoader} offers isolation, which can be overridden, by first delegating
 * the class search to its parent platform or bootstrap class loader. For classes that are not
 * found and not meant to be loaded in isolation, this plugin class loader delegates the loading
 * to gluon's class loader. If the classes were not found or were meant to be loaded in isolation,
 * the plugin class loader loads the classes from the plugin jar.</p>
 *
 * <p>Resource loading is allowed only from within the plugin and external system class loader
 * resources are not visible.</p>
 *
 * <p>Future changes might allow white-listing of packages and resources which could be loaded using
 * the gluon's class loader.</p>
 */

public class PluginClassLoader extends URLClassLoader {

    private static final ClassLoader parent;

    private final ClassLoader gluonClassLoader;

    private final boolean isGluonLoadingAllowed;

    static {
        // The delegation class loader is taken to be the bootstrap class loader if platform(extension) class loader
        // is not available.
        ClassLoader delegationClassLoader = null;

        try {
            delegationClassLoader = (ClassLoader) ClassLoader.class.getMethod("getPlatformClassLoader")
                    .invoke(null);
        } catch (NoSuchMethodException e) {
            // Java 8 or lower does not have this method, thus, we revert to the bootstrap class loader.
        } catch (Exception e) {
            throw new IllegalStateException("Cannot obtain platform class loader on Java 9+", e);
        }
        parent = delegationClassLoader;

        ClassLoader.registerAsParallelCapable();
    }

    public PluginClassLoader(URL[] pluginResourceURLs, ClassLoader gluonClassLoader) {
        this(pluginResourceURLs, gluonClassLoader, false);
    }

    public PluginClassLoader(URL[] pluginResourceURLs, ClassLoader gluonClassLoader, boolean gluonPackageLoading) {
        super(pluginResourceURLs, parent);
        this.gluonClassLoader = gluonClassLoader;
        this.isGluonLoadingAllowed = gluonPackageLoading;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);

            if (isGluonLoadingAllowed && loadedClass == null)
                try {
                    loadedClass = gluonClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) { }

            if (loadedClass == null)
                loadedClass = super.loadClass(name, false);

            if (resolve) {
                resolveClass(loadedClass);
            }

            return loadedClass;
        }
    }
}
