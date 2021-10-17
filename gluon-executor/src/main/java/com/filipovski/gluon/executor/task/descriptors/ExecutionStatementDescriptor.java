package com.filipovski.gluon.executor.task.descriptors;

import com.filipovski.gluon.executor.task.ExecutionStatement;

import java.util.Map;

import static com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptorComposer.STATEMENT;
import static java.util.Map.entry;

/**
 * Descriptor for an {@link ExecutionStatement} task.
 */

public class ExecutionStatementDescriptor extends TaskDescriptor {

    String statement;

    public ExecutionStatementDescriptor(String taskId, String statement) {
        super(taskId, ExecutionStatement.class.getName());
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }

    @Override
    public Map<String, String> toDescriptorMap() {
        return Map.ofEntries(
                entry(STATEMENT, statement)
        );
    }
}
