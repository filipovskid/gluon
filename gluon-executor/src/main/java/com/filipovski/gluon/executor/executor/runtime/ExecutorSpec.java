package com.filipovski.gluon.executor.executor.runtime;

import com.filipovski.gluon.executor.executor.ExecutorProvider;
import com.typesafe.config.Config;

/**
 * Specification for building an {@link com.filipovski.gluon.executor.executor.Executor}.
 */

public class ExecutorSpec {

    private final String name;

    private final ExecutorProvider executorProvider;

    private final Config configuration;

    public ExecutorSpec(String name, ExecutorProvider executorProvider, Config config) {
        this.name = name;
        this.executorProvider = executorProvider;
        this.configuration = config;
    }

    public String getName() {
        return name;
    }

    public ExecutorProvider getExecutorProvider() {
        return executorProvider;
    }

    public Config getConfiguration() {
        return configuration;
    }
}
