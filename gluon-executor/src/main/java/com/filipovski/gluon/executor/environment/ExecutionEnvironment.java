package com.filipovski.gluon.executor.environment;

import com.filipovski.gluon.executor.task.Task;

/**
 * {@link ExecutionEnvironment} facilitates the execution of {@link Task}s.
 * This environment manages remote executors and their execution environment.
 *
 * <p>Each {@link ExecutionEnvironment} manages a process where {@link Task}s are
 * executed. The process can be running on any instance e.g. local machine,
 * container or any environment which allows starting a process.</p>
 */

public interface ExecutionEnvironment {

    /**
     * Prepare the environment for task execution. This is the point where the
     * process this environment represents can be started.
     */
    void start();

    /**
     * Execute the {@link Task} within an external process.
     *
     * @param task
     */
    void execute(Task task);

    /**
     * Release the resources taken by the {@link ExecutionEnvironment}.
     */
    void stop();
}
