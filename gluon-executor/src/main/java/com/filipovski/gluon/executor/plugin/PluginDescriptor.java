package com.filipovski.gluon.executor.plugin;

import java.net.URL;

/**
 * Class containing metadata for plugin loading.
 */

public class PluginDescriptor {

    private final String pluginId;

    private final URL[] pluginResourceURLs;

    private final URL[] pluginMetadataURLs;

    public PluginDescriptor(String pluginId, URL[] pluginResourceURLs, URL[] pluginMetadataURLs) {
        this.pluginId = pluginId;
        this.pluginResourceURLs = pluginResourceURLs;
        this.pluginMetadataURLs = pluginMetadataURLs;
    }

    public static PluginDescriptor create(String pluginId, URL[] pluginResourceURLs) {
        return create(pluginId, pluginResourceURLs, new URL[0]);
    }

    public static PluginDescriptor create(String pluginId, URL[] pluginResourceURLs, URL[] pluginMetadataURLs) {
        return new PluginDescriptor(pluginId, pluginResourceURLs, pluginMetadataURLs);
    }

    public String getPluginId() {
        return pluginId;
    }

    public URL[] getPluginResourceURLs() {
        return pluginResourceURLs;
    }

    public URL[] getPluginMetadataURLs() {
        return pluginMetadataURLs;
    }
}
