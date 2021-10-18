package com.filipovski.gluon.executor.task.descriptors;

import com.filipovski.gluon.executor.task.ExecutionStatement;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Map.entry;

/**
 * Factory for creating {@link TaskDescriptor}s from a set of string-based properties.
 * This class facilitates the creation of task descriptions from task description data
 * coming as a result of task delegation to remote systems. It should primarily be used
 * for obtaining task descriptors within a remote runtime setting, where they will be
 * utilized for creating corresponding types of tasks to be executed within the environment.
 *
 * <p>Introduction of new {@link TaskDescriptor} implementations, as a result of supporting
 * different {@link com.filipovski.gluon.executor.task.Task}s, requires creating and registering
 * a corresponding factory method within {@link TaskDescriptorFactory}.</p>
 *
 * <p>It should be noted that task descriptors use the class name of their corresponding tasks
 * for their instantiation. Common mistake could be the use of task descriptor class names, which
 * would result in inability to locate factory methods. Check {@link TaskDescriptor} for insight
 * into the specific parameters.</p>
 */

public class TaskDescriptorFactory {

    private static final Map<String, Function<Map<String, String>, TaskDescriptor>> descriptorFactories = Map.ofEntries(
            entry(ExecutionStatement.class.getName(), TaskDescriptorFactory::createExecutionStatementDescriptor)
    );

    public static TaskDescriptor create(Map<String, String> descriptorProperties) {
        String taskClassName = descriptorProperties.get(TaskDescriptorComposer.TASK_CLASS_NAME);

        // TODO: Possible NullPointerException
        return descriptorFactories.get(taskClassName)
                .apply(descriptorProperties);
    }

    public static ExecutionStatementDescriptor createExecutionStatementDescriptor(Map<String, String> descriptorProperties) {
        ExecutionStatementDescriptorComposer composer = new ExecutionStatementDescriptorComposer();

        return composer.compose(descriptorProperties);
    }

}
