package com.filipovski.gluon.executor.executor;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;

import java.util.Optional;

/**
 * Implementations of this interface track and manage {@link Executor}s.
 *
 * <p>Current implementation provides the means for obtaining executors. Future
 * improvements should introduce the ability to react to executor state changes,
 * respond to lifecycle changes or update executor configuration.</p>
 */

public interface ExecutorManager {

    /**
     * Obtain an executor given its identifier. The executor identifier could follow
     * the naming scheme of using configuration details introduced with the plugin,
     * however, this depends on the manager's use-case and implementation details.
     *
     * @param executorIdentifier identifier used for obtaining an executor.
     * @return Executor for the provided identifier.
     */
    Optional<Executor> obtainExecutor(String executorIdentifier, ExecutionEnvironment environment);

    /** Stop all managed executors. */
    void stop();
}
