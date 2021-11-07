package com.filipovski.gluon.spark;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.environment.runtime.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.ExecutionContext;
import com.filipovski.gluon.executor.executor.ExecutionData;
import com.filipovski.gluon.python.PythonExecutor;
import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PySparkExecutor extends PythonExecutor {

    private final Logger logger = LogManager.getLogger(PySparkExecutor.class);

    private final String PYSPARK_INITIALIZATION_SCRIPT = "/runtime/pyspark_init.py";

    private final RuntimeEnvironment environment;

    private final Config config;

    public PySparkExecutor(ExecutionEnvironment environment, Config config) {
        super(environment, config);

        this.environment = (RuntimeEnvironment) environment;
        this.config = config;
    }

    @Override
    public void start() {
        super.start();
        try {
            prepareExecutor();
        } catch (IOException e) {
            logger.error("An error occurred while trying to initialize PySpark executor! [{}]", e.toString());
            stop();
            return;
        }
        
        logger.info("PySpark executor started successfully.");
    }

    private void prepareExecutor() throws IOException {
        InputStream scriptStream = getClass().getResourceAsStream(PYSPARK_INITIALIZATION_SCRIPT);
        String initCode = new String(scriptStream.readAllBytes(), StandardCharsets.UTF_8);
        ExecutionData executionData = new ExecutionData(initCode);
        ExecutionContext executionContext = new ExecutionContext(
                "pyspark-init-task",
                (executionOutputData) -> logger.info("Initialization output [{}]", executionOutputData.serialize()),
                environment.getRuntimeContext()
        );
        execute(executionData, executionContext);
    }
}
