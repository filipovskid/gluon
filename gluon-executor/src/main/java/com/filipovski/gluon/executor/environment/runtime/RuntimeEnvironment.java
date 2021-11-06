package com.filipovski.gluon.executor.environment.runtime;

import com.filipovski.gluon.executor.environment.AbstractEnvironment;
import com.filipovski.gluon.executor.executor.ExecutorManager;
import com.filipovski.gluon.executor.executor.output.ExecutionOutputWriter;
import com.filipovski.gluon.executor.task.Task;
import com.filipovski.gluon.executor.task.TaskExecutionActions;

/**
 *
 */

public class RuntimeEnvironment extends AbstractEnvironment {

    private final ExecutorManager executorManager;

    private final TaskExecutionActions taskExecutionActions;

    private final RuntimeContext runtimeContext;

    public RuntimeEnvironment(ExecutorManager executorManager,
                              TaskExecutionActions taskExecutionActions,
                              ExecutionOutputWriter executionOutputWriter) {
        this.executorManager = executorManager;
        this.taskExecutionActions = taskExecutionActions;
        this.runtimeContext = new RuntimeContext(taskExecutionActions, executorManager, executionOutputWriter);
    }

    @Override
    public void start() {

    }

    @Override
    public void execute(Task task) {
        task.run();
    }

    @Override
    public void stop() {
        executorManager.stop();
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
