package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.runtime.RuntimeContext;
import com.filipovski.gluon.executor.environment.runtime.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.ExecutionContext;
import com.filipovski.gluon.executor.executor.ExecutionData;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.executor.ExecutorManager;
import com.filipovski.gluon.executor.executor.output.ExecutionOutput;
import com.filipovski.gluon.executor.executor.output.ExecutionOutputWriter;
import com.filipovski.gluon.executor.executor.output.TaskExecutionOutputWriter;
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

    private final TaskExecutionActions taskExecutionActions;

    private final ExecutorManager executorManager;

    private final ExecutionOutputWriter executionOutputWriter;

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
        this.taskExecutionActions = runtimeContext.getTaskExecutionActions();
        this.executorManager = runtimeContext.getExecutorManager();
        this.executionOutputWriter = runtimeContext.getExecutionOutputWriter();
    }

    @Override
    protected void doRun() {
        // TODO: Implement cancellation. Task transition should be atomic if multiple threads are involved.

        updateState(TaskStatus.RUNNING);

        ExecutionData executionData = createExecutionData();
        ExecutionContext executionContext = createExecutionContext();

        try {
            executor = executorManager.obtainExecutor(executorIdentifier, getEnvironment())
                    .orElseThrow(this::handleExecutorNotFound);
            executor.execute(executionData, executionContext);

            updateState(TaskStatus.COMPLETED);
        } catch(Exception e) {
            updateState(TaskStatus.FAILED);
        }
    }

    private void updateState(TaskStatus status) {
        if (transitionState(status)) {
            taskExecutionActions.updateTaskExecutionState(new TaskExecutionState(getId(), status));
        }
    }

    private ExecutionData createExecutionData() {
        return new ExecutionData(statement);
    }

    private ExecutionContext createExecutionContext() {
        TaskExecutionOutputWriter outputWriter = (executionOutputData) -> {
            ExecutionOutput executionOutput = new ExecutionOutput(getId(), executionOutputData);
            executionOutputWriter.write(executionOutput);
        };

        return new ExecutionContext(getId(), outputWriter, runtimeContext);
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
