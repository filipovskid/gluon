package com.filipovski.gluonserver.task;

import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.task.Task;


import java.util.Objects;

/**
 * A {@link Task} responsible for executing a statement.
 */

public class ExecutionStatement extends Task<ExecutionStatementResult> {

    private String statement;

    private Executor executor;

    private ExecutionStatement(String taskId, String statement, Executor executor) {
        super(taskId);

        this.statement = statement;
        this.executor = Objects.requireNonNull(executor);
    }

    @Override
    public void doRun() {
        // TODO: Implement task execution
    }

    public static class Builder {

        private String taskId;
        private String statement;
        private Executor executor;

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder statement(String statement) {
            this.statement = statement;
            return this;
        }

        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public ExecutionStatement build() {
            return new ExecutionStatement(taskId, statement, executor);
        }
    }


}
