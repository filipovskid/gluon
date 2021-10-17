package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.environment.RuntimeEnvironment;
import com.google.common.base.Strings;

import java.util.Objects;

/**
 * {@link Task} is a unit of execution managed by Gluon. Extending
 * this class requires implementing the {@link #doRun()} method which
 * contains the processing logic executed during the {@link Task} execution.
 *
 * <p>Execution of the {@link Task} starts by invoking the {@link #run()} method.</p>
 */

public abstract class Task<T extends TaskResult> {

    private String taskId;

    private ExecutionEnvironment environment;

    private TaskStatus taskStatus;

    private T result;

    public Task(String taskId, ExecutionEnvironment environment) {
        if (Strings.isNullOrEmpty(taskId))
            throw new IllegalArgumentException("taskId must not be empty.");

        this.taskId = taskId;
        this.environment = Objects.requireNonNull(environment);
        this.taskStatus = TaskStatus.CREATED;
        this.result = null;
    }

    /**
     * Execution logic of the task.
     */
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
