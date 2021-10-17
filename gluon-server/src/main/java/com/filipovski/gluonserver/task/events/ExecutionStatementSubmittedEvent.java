package com.filipovski.gluonserver.task.events;

import com.filipovski.gluon.executor.task.ExecutionStatement;

import java.util.Objects;

/**
 * {@link ExecutionStatement} request arrival event.
 */

public class ExecutionStatementSubmittedEvent {

    private final String taskId;
    private final String executorIdentifier;
    private final String statement;

    public ExecutionStatementSubmittedEvent(String taskId,
                                            String executorIdentifier,
                                            String statement) {
        this.taskId = Objects.requireNonNull(taskId);
        this.executorIdentifier = Objects.requireNonNull(executorIdentifier);
        this.statement = Objects.requireNonNull(statement);
    }

    public String getTaskId() {
        return taskId;
    }

    public String getExecutorIdentifier() {
        return executorIdentifier;
    }

    public String getStatement() {
        return statement;
    }
}
