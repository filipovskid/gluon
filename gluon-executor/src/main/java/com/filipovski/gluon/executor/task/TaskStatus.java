package com.filipovski.gluon.executor.task;

/**
 * Represents the states in which a {@link Task} can be after its creation.
 */

public enum TaskStatus {
    /** The task is newly created and has not been executed jet. */
    CREATED,

    /** Task is dispatched for execution, but has not started running yet. */
    PENDING,

    /** The task is being executed. */
    RUNNING,

    /** Task is being cancelled. */
    CANCELLING,

    /** The task has been successfully executed. */
    COMPLETED,

    /** Task has been canceled. */
    CANCELED,

    /** The task has failed. */
    FAILED
}
