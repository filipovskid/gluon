package com.filipovski.gluon.python;

import com.filipovski.gluon.executor.configuration.ExecutorConfigOptions;
import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.executor.ExecutorProvider;
import com.typesafe.config.Config;

import java.util.Map;

import static java.util.Map.entry;

import java.util.function.BiFunction;

public class PythonExecutorProvider implements ExecutorProvider {

    Map<String, BiFunction<ExecutionEnvironment, Config, Executor>> executorFactories = Map.ofEntries(
            entry(PythonExecutor.class.getName(), this::createPythonExecutor),
            entry("com.filipovski.gluon.python.IPythonExecutor", this::createPythonExecutor)
    );

    @Override
    public Executor create(ExecutionEnvironment environment, Config executorConfig) {
        String executorClassName = executorConfig.getString(ExecutorConfigOptions.EXECUTOR_CLASS_NAME);

        // TODO: NullPointerException
        return executorFactories.get(executorClassName)
                .apply(environment, executorConfig);
    }

    private Executor createPythonExecutor(ExecutionEnvironment environment, Config executorConfig) {
        return new PythonExecutor(environment, executorConfig);
    }
}
