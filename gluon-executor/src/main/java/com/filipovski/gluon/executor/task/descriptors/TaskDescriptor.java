package com.filipovski.gluon.executor.task.descriptors;

import java.util.Map;

/**
 * {@link TaskDescriptor} describes a specific Task type with a set of string-based
 * properties for easier task delegation across remote systems. Implementations of
 * this abstract class need to provide an implementation for {@link TaskDescriptor#toDescriptorMap()}.
 *
 * <p>{@link TaskDescriptor#toDescriptorMap()} implementation needs to encapsulate all
 * task properties necessary for creating the corresponding task. Each task descriptor
 * requires a corresponding {@link TaskDescriptorComposer} implementation for validating
 * and recreating the descriptor given a set of properties.</p>
 */

public abstract class TaskDescriptor {

    private String taskId;

    private String taskClassName;

    public TaskDescriptor(String taskId, String taskClassName) {
        this.taskId = taskId;
        this.taskClassName = taskClassName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskClassName() {
        return taskClassName;
    }

    public abstract Map<String, String> toDescriptorMap();
}
