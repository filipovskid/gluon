package com.filipovski.gluon.executor.environment.runtime;

import com.filipovski.gluon.executor.environment.AbstractEnvironment;
import com.filipovski.gluon.executor.executor.ExecutorManager;
import com.filipovski.gluon.executor.task.Task;

/**
 *
 */

public class RuntimeEnvironment extends AbstractEnvironment {

    private final ExecutorManager executorManager;

    private final RuntimeContext runtimeContext;

    public RuntimeEnvironment(ExecutorManager executorManager) {
        this.executorManager = executorManager;
        this.runtimeContext = new RuntimeContext(executorManager);
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

    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
