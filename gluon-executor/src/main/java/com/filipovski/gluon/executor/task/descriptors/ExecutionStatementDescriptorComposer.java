package com.filipovski.gluon.executor.task.descriptors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Map;

import static java.util.Map.entry;

/**
 * Composer for the corresponding {@link ExecutionStatementDescriptor}.
 */

public class ExecutionStatementDescriptorComposer implements TaskDescriptorComposer {

    public static final String STATEMENT_TASK_PREFIX = "execution-statement-task";

    public static final String STATEMENT = "execution-statement-task.statement";

    private static final Config referenceConfig;

    static {
        Map<String, String> referenceProperties = Map.ofEntries(
                entry(TASK_ID, "task-id"),
                entry(TASK_CLASS_NAME, "class-name"),
                entry(STATEMENT, "statement")
        );

        referenceConfig = ConfigFactory.parseMap(referenceProperties);
    }

    @Override
    public ExecutionStatementDescriptor compose(Map<String, String> descriptorProperties) {
        Config descriptorConfig = ConfigFactory.parseMap(descriptorProperties);
        descriptorConfig.checkValid(referenceConfig, STATEMENT_TASK_PREFIX);

        return new ExecutionStatementDescriptor(
                descriptorConfig.getString(TASK_ID),
                descriptorConfig.getString(STATEMENT)
        );
    }
}
