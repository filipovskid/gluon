package com.filipovski.gluonserver.task.events;

import com.filipovski.common.domain.DomainEvent;

import java.time.Instant;
import java.util.Objects;

/**
 * Even created at the arrival of an integration event for executing an
 * {@link com.filipovski.gluon.executor.task.ExecutionStatement} task.
 */

public class ExecutionStatementSubmittedEvent implements DomainEvent {

    private final String taskId;

    private final String sessionId;

    private final String executorIdentifier;

    private final String statement;

    private final Instant timestamp;

    public ExecutionStatementSubmittedEvent(String taskId,
                                            String sessionId,
                                            String executorIdentifier,
                                            String statement,
                                            Instant timestamp) {
        this.taskId = Objects.requireNonNull(taskId, "taskId must not be null!");
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId must not be null!");
        this.executorIdentifier = Objects.requireNonNull(executorIdentifier, "executorIdentifier must not be null!");
        this.statement = Objects.requireNonNull(statement, "statement must not be null!");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null!");
    }

    public String getTaskId() {
        return taskId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getExecutorIdentifier() {
        return executorIdentifier;
    }

    public String getStatement() {
        return statement;
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }
}
