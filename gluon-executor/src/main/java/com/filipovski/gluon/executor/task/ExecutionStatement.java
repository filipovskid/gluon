package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptor;


import java.util.Objects;

/**
 * A {@link Task} responsible for executing a statement.
 */

public class ExecutionStatement extends Task {

    private String statement;

    private ExecutionStatement(String taskId,
                               String statement,
                               ExecutionStatementDescriptor taskDescriptor,
                               RuntimeEnvironment environment) {
        super(taskId, taskDescriptor, environment);

        this.statement = statement;
    }

    @Override
    public void doRun() {
        // TODO: Implement task execution
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String taskId;
        private String statement;
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

        public ExecutionStatement build() {
            return new ExecutionStatement(taskId, statement, taskDescriptor, environment);
        }
    }


}
