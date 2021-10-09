package com.filipovski.gluon.executor.task;

import java.time.Duration;

/**
 * Abstract base class for the results produced by {@link Task} execution.
 * This class contains the common information relevant for all Task executions.
 */

public abstract class TaskResult {

    private final String taskId;

    /** ime elapsed from task execution start time to its completion. */
    private final Duration runtimeDuration;

    /**  Whether task's execution has failed due to internal issues. */
    private final boolean failed;

    public TaskResult(String taskId, Duration runtimeDuration, boolean hasFailed) {
        this.taskId = taskId;
        this.runtimeDuration = runtimeDuration;
        this.failed = hasFailed;
    }

    public String getTaskId() {
        return taskId;
    }

    public Duration getRuntimeDuration() {
        return runtimeDuration;
    }

    public boolean hasFailed() {
        return failed;
    }
}
