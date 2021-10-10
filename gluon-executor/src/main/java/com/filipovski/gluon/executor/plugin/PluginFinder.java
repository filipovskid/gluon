package com.filipovski.gluon.executor.plugin;

import java.util.List;

/**
 * Implementations of this interface provide different strategies for locating plugins.
 * Each found plugin is described with a {@link PluginDescriptor}.
 */

public interface PluginFinder {

    List<PluginDescriptor> findPlugins();
}
