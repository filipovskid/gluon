package com.filipovski.gluon.executor.executor.output;

/**
 * Data obtained at runtime as a consequence of execution. This class encapsulates
 * the execution data along with task information necessary for its propagation to
 * gluon server.
 */

public class ExecutionOutput {

    private final String taskId;

    private final ExecutionOutputData data;

    public ExecutionOutput(String taskId, ExecutionOutputData data) {
        this.taskId = taskId;
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public ExecutionOutputData getData() {
        return data;
    }
}
