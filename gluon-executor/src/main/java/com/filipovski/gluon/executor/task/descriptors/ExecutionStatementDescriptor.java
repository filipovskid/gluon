package com.filipovski.gluon.executor.task.descriptors;

import com.filipovski.gluon.executor.task.ExecutionStatement;

import java.util.Map;

import static com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptorComposer.EXECUTOR_IDENTIFIER;
import static com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptorComposer.STATEMENT;
import static com.filipovski.gluon.executor.task.descriptors.TaskDescriptorComposer.TASK_CLASS_NAME;
import static com.filipovski.gluon.executor.task.descriptors.TaskDescriptorComposer.TASK_ID;
import static java.util.Map.entry;

/**
 * Descriptor for an {@link ExecutionStatement} task.
 */

public class ExecutionStatementDescriptor extends TaskDescriptor {

    private final String statement;

    private final String executorIdentifier;

    public ExecutionStatementDescriptor(String taskId, String statement, String executorIdentifier) {
        super(taskId, ExecutionStatement.class.getName());

        this.statement = statement;
        this.executorIdentifier = executorIdentifier;
    }

    public String getStatement() {
        return statement;
    }

    public String getExecutorIdentifier() {
        return executorIdentifier;
    }

    @Override
    public Map<String, String> toDescriptorMap() {
        return Map.ofEntries(
                entry(TASK_ID, getTaskId()),
                entry(TASK_CLASS_NAME, getTaskClassName()),
                entry(STATEMENT, statement),
                entry(EXECUTOR_IDENTIFIER, executorIdentifier)
        );
    }
}
