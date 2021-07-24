package com.filipovski.gluonexecutor.task;

/**
 * {@link Task} is a unit of execution managed by Gluon. Extending
 * this class requires implementing the {@link #doRun()} method which
 * contains the processing logic executed during the {@link Task} execution.
 *
 * <p>Execution of the {@link Task} starts by invoking the {@link #run()} method.</p>
 */

public abstract class Task<T> {

    private String taskId;
    private TaskStatus taskStatus;
    private T result;

    public Task(String taskId) {
        this.taskId = taskId;
        this.taskStatus = TaskStatus.CREATED;
        this.result = null;
    }

    /** Execution logic of the task. */
    public abstract void doRun();

    public final void run() {
        doRun();
    }

    public String getId() {
        return taskId;
    }

    public T getResult() {
        return result;
    }
}
