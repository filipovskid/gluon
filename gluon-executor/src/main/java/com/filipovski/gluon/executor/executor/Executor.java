package com.filipovski.gluon.executor.executor;

/**
 * {@link Executor} is responsible for executing supported commands.
 *
 * <p>{@link Executor}s are instances that run computations in different
 * programming languages, processing frameworks, analytics engines etc.</p>
 */

public interface Executor {

    /** Starts this executor within an execution driver. */
    void start();

    /** Executes a command within executor's execution process. */
    void execute(ExecutionData data, ExecutionContext executionContext);

    /**
     * Shuts down the executor and releases all the resources acquired during
     * its startup.
     */
    void stop();
}
