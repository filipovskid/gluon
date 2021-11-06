package com.filipovski.gluonserver.task.events;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;

import java.time.Instant;
import java.util.Objects;

/**
 * Event created at the arrival of execution output data. This data does not refer
 * to the task result, instead, it represents the stream of data produced over the
 * duration of the task execution.
 *
 * <p>Execution output is currently not treated by gluon server, so it is directly
 * propagated to the task dispatching service.</p>
 */

public class ExecutionOutputArrivedEvent implements DomainEvent {

    private final String taskId;

    private final String data;

    private final Instant occuredOn;

    private ExecutionOutputArrivedEvent(String taskId, String data, Instant occuredOn) {
        if(Strings.isNullOrEmpty(taskId))
            throw new IllegalArgumentException("taskId must not be empty");

        if(Strings.isNullOrEmpty(data))
            throw new IllegalArgumentException("data must not be empty");

        this.taskId = taskId;
        this.data = data;
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static ExecutionOutputArrivedEvent from(String taskId, String data, Instant occuredOn) {
        return new ExecutionOutputArrivedEvent(taskId, data, occuredOn);
    }

    public String getTaskId() {
        return taskId;
    }

    public String getData() {
        return data;
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
