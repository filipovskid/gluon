package com.filipovski.gluon.executor.executor;

import com.filipovski.gluon.executor.environment.runtime.RuntimeContext;

/**
 * Contextual information about an execution. Each task or even an execution within
 * a task will have a context through which the {@link Executor} will have access to
 * contextual information or execution-specific constructs.
 */

public class ExecutionContext {

    public String taskId;

    public RuntimeContext runtimeContext;

    public ExecutionContext(String taskId, RuntimeContext runtimeContext) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
