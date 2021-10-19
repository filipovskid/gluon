package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptor;
import com.google.common.base.Strings;

import java.util.Objects;

/**
 * {@link Task} is a unit of execution managed by Gluon. Extending
 * this class requires implementing the {@link #doRun()} method which
 * contains the processing logic executed during the {@link Task} execution.
 *
 * <p>Execution of the {@link Task} starts by invoking the {@link #run()} method.</p>
 */

public abstract class Task {

    private String taskId;

    private ExecutionEnvironment environment;

    private TaskDescriptor taskDescriptor;

    private TaskStatus taskStatus;

    public Task(String taskId, TaskDescriptor taskDescriptor, ExecutionEnvironment environment) {
        if (Strings.isNullOrEmpty(taskId))
            throw new IllegalArgumentException("taskId must not be empty.");

        this.taskId = taskId;
        this.taskDescriptor = taskDescriptor;
        this.environment = Objects.requireNonNull(environment);
        this.taskStatus = TaskStatus.CREATED;
    }

    /**
     * Task execution logic.
     */
    protected abstract void doRun();

    public final void run() {
        doRun();
    }

    public String getId() {
        return taskId;
    }

    public TaskDescriptor getTaskDescriptor() {
        return taskDescriptor;
    }

    public ExecutionEnvironment getEnvironment() {
        return environment;
    }

}
