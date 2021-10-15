package com.filipovski.gluon.executor.executor.runtime;

import com.filipovski.gluon.executor.executor.ExecutorProvider;
import com.filipovski.gluon.executor.plugin.PluginDescriptor;
import com.typesafe.config.Config;

/**
 * Specification for building an {@link com.filipovski.gluon.executor.executor.Executor}.
 */

public class ExecutorSpec {

    private final String name;

    private final PluginDescriptor pluginDescriptor;

    private final ExecutorProvider executorProvider;

    private final Config configuration;

    public ExecutorSpec(String name, PluginDescriptor pluginDescriptor, ExecutorProvider executorProvider, Config config) {
        this.name = name;
        this.pluginDescriptor = pluginDescriptor;
        this.executorProvider = executorProvider;
        this.configuration = config;
    }

    public String getName() {
        return name;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    public ExecutorProvider getExecutorProvider() {
        return executorProvider;
    }

    public Config getConfiguration() {
        return configuration;
    }
}
