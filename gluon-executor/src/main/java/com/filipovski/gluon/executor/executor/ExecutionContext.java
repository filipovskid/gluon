package com.filipovski.gluon.executor.executor;

import com.filipovski.gluon.executor.environment.runtime.RuntimeContext;
import com.filipovski.gluon.executor.executor.output.TaskExecutionOutputWriter;

/**
 * Contextual information about an execution. Each task or even an execution within
 * a task will have a context through which the {@link Executor} will have access to
 * contextual information or execution-specific constructs.
 */

public class ExecutionContext {

    private final String taskId;

    private final TaskExecutionOutputWriter taskOutputWriter;

    private final RuntimeContext runtimeContext;

    public ExecutionContext(String taskId, TaskExecutionOutputWriter taskOutputWriter, RuntimeContext runtimeContext) {
        this.taskId = taskId;
        this.taskOutputWriter = taskOutputWriter;
        this.runtimeContext = runtimeContext;
    }

    public String getTaskId() {
        return taskId;
    }

    public TaskExecutionOutputWriter getTaskOutputWriter() {
        return taskOutputWriter;
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
