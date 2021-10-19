package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.runtime.RuntimeContext;
import com.filipovski.gluon.executor.environment.runtime.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.ExecutionContext;
import com.filipovski.gluon.executor.executor.ExecutionData;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.executor.ExecutorManager;
import com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link Task} responsible for executing a statement.
 */

// TODO: Add logging information for environment when executor is not found.

public class ExecutionStatement extends Task {

    private final Logger logger = LogManager.getLogger(ExecutionStatement.class);

    private final String statement;

    private final String executorIdentifier;

    private final RuntimeContext runtimeContext;

    private final ExecutorManager executorManager;

    private Executor executor;

    private ExecutionStatement(String taskId,
                               String statement,
                               String executorIdentifier,
                               ExecutionStatementDescriptor taskDescriptor,
                               RuntimeEnvironment environment) {
        super(taskId, taskDescriptor, environment);

        this.statement = statement;
        this.executorIdentifier = executorIdentifier;
        this.runtimeContext = environment.getRuntimeContext();
        this.executorManager = runtimeContext.getExecutorManager();
    }

    @Override
    protected void doRun() {
        // TODO: Implement cancellation. Task transition should be atomic if multiple threads are involved.

        ExecutionData executionData = createExecutionData();
        ExecutionContext executionContext = createExecutionContext();

        executor = executorManager.obtainExecutor(executorIdentifier, getEnvironment())
                .orElseThrow(this::handleExecutorNotFound);
        executor.execute(executionData, executionContext);
    }

    private ExecutionData createExecutionData() {
        return new ExecutionData(statement);
    }

    private ExecutionContext createExecutionContext() {
        return new ExecutionContext(getId(), runtimeContext);
    }

    private RuntimeException handleExecutorNotFound() {
        logger.warn("Executor with identifier [{}] could not be found within environment [{}] and session [{}]!",
                executorIdentifier, "TODO", "TODO");

        return new RuntimeException("Executor with identifier [%s] not found!".formatted(executorIdentifier));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String taskId;
        private String statement;
        private String executorIdentifier;
        private RuntimeEnvironment environment;
        private ExecutionStatementDescriptor taskDescriptor;

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder statement(String statement) {
            this.statement = statement;
            return this;
        }

        public Builder environment(RuntimeEnvironment environment) {
            this.environment = environment;
            return this;
        }

        public Builder descriptor(ExecutionStatementDescriptor taskDescriptor) {
            this.taskDescriptor = taskDescriptor;
            return this;
        }

        public Builder executor(String executorIdentifier) {
            this.executorIdentifier = executorIdentifier;
            return this;
        }

        public ExecutionStatement build() {
            return new ExecutionStatement(taskId, statement, executorIdentifier, taskDescriptor, environment);
        }
    }


}