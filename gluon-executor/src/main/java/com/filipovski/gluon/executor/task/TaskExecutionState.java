package com.filipovski.gluon.executor.task;

public class TaskExecutionState {

    private final String taskId;

    private final TaskStatus status;

    private final TaskResult result;

    public TaskExecutionState(String taskId, TaskStatus taskStatus) {
        this(taskId, taskStatus, null);
    }

    public TaskExecutionState(String taskId, TaskStatus status, TaskResult result) {
        this.taskId = taskId;
        this.status = status;
        this.result = result;
    }

    public String getTaskId() {
        return taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskResult getResult() {
        return result;
    }
}
