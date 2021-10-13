package com.filipovski.gluon.executor.executor;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.typesafe.config.Config;

public interface ExecutorProvider {

    Executor create(ExecutionEnvironment environment, Config executorConfig);

}
