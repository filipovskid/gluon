package com.filipovski.gluon.spark;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.executor.ExecutionContext;
import com.filipovski.gluon.executor.executor.ExecutionData;
import com.filipovski.gluon.python.PythonExecutor;
import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PySparkExecutor extends PythonExecutor {

    private final Logger logger = LogManager.getLogger(PySparkExecutor.class);

    public PySparkExecutor(ExecutionEnvironment environment, Config config) {
        super(environment, config);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void execute(ExecutionData data, ExecutionContext executionContext) {
//        super.execute(data, executionContext);
        logger.info("[{}] executor execute method has been called.", this.getClass().getName());
    }
}
