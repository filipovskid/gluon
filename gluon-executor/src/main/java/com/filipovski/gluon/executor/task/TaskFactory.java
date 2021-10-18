package com.filipovski.gluon.executor.task;

import com.filipovski.gluon.executor.environment.RuntimeEnvironment;
import com.filipovski.gluon.executor.task.descriptors.ExecutionStatementDescriptor;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptor;

import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Map.entry;

/**
 * Factory for creating {@link Task}s from a given {@link TaskDescriptor} and a runtime
 * environment. This class should be used for creating tasks in a remote runtime setting,
 * where the {@link com.filipovski.gluon.executor.environment.remote.ExecutionEnvironmentDriver}
 * executes the task within a {@link RuntimeEnvironment}.
 *
 * <p>Supporting different {@link Task} implementations requires creating and registering a corresponding
 * factory method within {@link TaskFactory}.</p>
 */

public class TaskFactory {

    private static final Map<String, BiFunction<TaskDescriptor, RuntimeEnvironment, Task>> taskFactories = Map.ofEntries(
            entry(ExecutionStatement.class.getName(), TaskFactory::createExecutionStatement)
    );

    public static Task create(TaskDescriptor taskDescriptor, RuntimeEnvironment environment) {

        // TODO: Possible NullPointerException
        return taskFactories.get(taskDescriptor.getTaskClassName())
                .apply(taskDescriptor, environment);
    }

    public static ExecutionStatement createExecutionStatement(TaskDescriptor taskDescriptor, RuntimeEnvironment environment) {
        ExecutionStatementDescriptor statementDescriptor = (ExecutionStatementDescriptor) taskDescriptor;

        return ExecutionStatement.newBuilder()
                .taskId(statementDescriptor.getTaskId())
                .statement(statementDescriptor.getStatement())
                .environment(environment)
                .build();
    }
}
