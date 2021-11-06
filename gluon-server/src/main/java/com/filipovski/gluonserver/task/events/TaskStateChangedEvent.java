package com.filipovski.gluonserver.task.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.executor.task.TaskStatus;
import com.google.common.base.Strings;

import java.time.Instant;
import java.util.Objects;

/**
 * Event created as a result of task state change. This event is a direct consequence
 * of execution-time task state update which arrives from a runtime environment.
 *
 * <p>This event is currently not treated by gluon server, instead it is propagated to
 * the service which dispatched the task.</p>
 */

public class TaskStateChangedEvent implements DomainEvent {

    private final String taskId;

    private final TaskStatus status;

    private final String result;

    private final Instant occuredOn;

    private TaskStateChangedEvent(String taskId, TaskStatus status, String result, Instant occuredOn) {
        if(Strings.isNullOrEmpty(taskId))
            throw new IllegalArgumentException("taskId must not be empty");

        this.taskId = taskId;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.result = result;
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static TaskStateChangedEvent from(String taskId, String taskStatus, String result, Instant occuredOn) {
        return new TaskStateChangedEvent(
                taskId,
                TaskStatus.valueOf(taskStatus),
                result,
                occuredOn
        );
    }

    public String getTaskId() {
        return taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
