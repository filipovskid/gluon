package com.filipovski.gluon.executor.executor;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.typesafe.config.Config;

/**
 * {@link ExecutorProvider} is a service povider for creating {@link Executor}s that
 * are available within the executor plugin. Each executor plugin must have a class
 * implementation of this interface to enable creating {@link Executor}.
 *
 * <p>{@link com.filipovski.gluon.executor.plugin.PluginLoader} utilizes a {@link java.util.ServiceLoader}
 * to locate the implementations of this interface within a plugin. To enable locating
 * these implementations a service provider must identified by placing a provider-configuration
 * file in the resource directory META-INF/services.
 *
 * Example: Suppose we have a service type com.filipovski.gluon.python.PythonExecutorProvider which is
 * intended to represent an executor provider within a python executor plugin. The jar of this plugin
 * should contain a file named:
 *
 * META-INF/services/com.filipovski.gluon.executor.executor.ExecutorProvider
 *
 * This file contains a single line identifying this executor provider:
 *
 * com.filipovski.gluon.python.PythonExecutorProvider
 * </p>
 */

public interface ExecutorProvider {

    Executor create(ExecutionEnvironment environment, Config executorConfig);

}
