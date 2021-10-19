package com.filipovski.gluon.executor.environment.runtime;

import com.filipovski.gluon.executor.executor.ExecutorManager;
import com.filipovski.gluon.executor.task.TaskExecutionActions;

/**
 * RuntimeContext contains information about the runtime in which tasks are executed.
 * A runtime is defined by its environment which encapsulates executors and facilitates
 * task execution through scheduling or other mechanisms. Each runtime provides a context
 * through which different components can access contextual information and other constructs
 * bounded to the corresponding environment and the use-case that the environment provides.
 *
 * <p>For example, when an environment is used to represent a notebook runtime, the corresponding
 * runtime context provides information about the environment and communication facilities for
 * sending output data to the notebook.</p>
 */

public class RuntimeContext {

    private final TaskExecutionActions taskExecutionActions;

    private final ExecutorManager executorManager;

    public RuntimeContext(TaskExecutionActions taskExecutionActions, ExecutorManager executorManager) {
        this.taskExecutionActions = taskExecutionActions;
        this.executorManager = executorManager;
    }

    public TaskExecutionActions getTaskExecutionActions() {
        return taskExecutionActions;
    }

    public ExecutorManager getExecutorManager() {
        return executorManager;
    }
}
