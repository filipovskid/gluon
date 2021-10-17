package com.filipovski.gluon.executor.task.descriptors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Map;

/**
 * Composer for the corresponding {@link ExecutionStatementDescriptor}.
 */

public class ExecutionStatementDescriptorComposer implements TaskDescriptorComposer {

    public static final String STATEMENT_TASK_PREFIX = "execution-statement-task";

    public static final String STATEMENT = "execution-statement-task.statement";

    private static final Config referenceConfig;

    static {
        Map<String, String> referenceProperties = Map.ofEntries(
                Map.entry(STATEMENT, "stmt")
        );

        referenceConfig = ConfigFactory.parseMap(referenceProperties);
    }

    @Override
    public ExecutionStatementDescriptor compose(String taskId, Map<String, String> descriptorProperties) {
        Config descriptorConfig = ConfigFactory.parseMap(descriptorProperties);
        descriptorConfig.checkValid(referenceConfig, STATEMENT_TASK_PREFIX);

        return new ExecutionStatementDescriptor(taskId, descriptorConfig.getString(STATEMENT));
    }
}
