package com.filipovski.gluon.executor.executor.runtime;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.environment.runtime.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.Executor;
import com.filipovski.gluon.executor.executor.ExecutorManager;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class is a runtime implementation of {@link ExecutorManager} used for creating and managing
 * {@link Executor}s within a {@link RuntimeEnvironment}. {@link RuntimeExecutorManager} guarantees
 * to maintain at most a single instance of a given type of executor within the environment driver
 * runtime environment.
 *
 * <p>An instance of this executor manager is meant to be used within a single runtime environment.
 * Managing executors for multiple environments can produce unexpected behaviour, which can result
 * with overridden or shared executors within independent environments.</p>
 *
 * <p>Current implementation utilizes in-memory {@link ConcurrentMap} store for executors' state.
 * This state is lost in case of machine failure, leaving running executors hanging. Future improvements
 * should ensure that this storage mode is complemented with a remote stable store.</p>
 */

// TODO: Executor state is lost in case of machine failure, leaving running executors hanging.
//       Future improvements should ensure that this storage mode is complemented with a remote
//       stable store.

public class RuntimeExecutorManager implements ExecutorManager {

    private final ExecutorLoader executorLoader;

    private final ConcurrentMap<String, Executor> environmentExecutors;

    public RuntimeExecutorManager(ExecutorLoader executorLoader) {
        this.executorLoader = executorLoader;
        this.environmentExecutors = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Executor> obtainExecutor(String executorIdentifier, ExecutionEnvironment environment) {
        Executor executor = environmentExecutors.computeIfAbsent(executorIdentifier,
                executorId -> executorLoader.createExecutor(executorIdentifier, environment).orElse(null));

        return Optional.ofNullable(executor);
    }
}
