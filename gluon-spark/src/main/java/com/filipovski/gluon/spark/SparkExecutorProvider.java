package com.filipovski.gluon.spark;

import com.filipovski.gluon.executor.configuration.ExecutorConfigOptions;
import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.executor.ExecutorProvider;
import com.typesafe.config.Config;

import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Map.entry;

public class SparkExecutorProvider implements ExecutorProvider {

    Map<String, BiFunction<ExecutionEnvironment, Config, Executor>> executorFactories = Map.ofEntries(
            entry(PySparkExecutor.class.getName(), this::createPySparkExecutor)
    );

    @Override
    public Executor create(ExecutionEnvironment environment, Config executorConfig) {
        String executorClassName = executorConfig.getString(ExecutorConfigOptions.EXECUTOR_CLASS_NAME);

        // TODO: NullPointerException
        return executorFactories.get(executorClassName)
                .apply(environment, executorConfig);
    }

    private Executor createPySparkExecutor(ExecutionEnvironment environment, Config executorConfig) {
        return new PySparkExecutor(environment, executorConfig);
    }
}
