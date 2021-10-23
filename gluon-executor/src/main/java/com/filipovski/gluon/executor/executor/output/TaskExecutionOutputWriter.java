package com.filipovski.gluon.executor.executor.output;

/**
 * Implementations of this class are used to handle the execution output data
 * of a single task or even an execution within a task.
 *
 * <p>The most common implementation provides data streaming to gluon server
 * through the use of {@link ExecutionOutputWriter}. When used in this fashion
 * the implementation should encapsulate task information to enable the construction
 * of {@link ExecutionOutputData}<./p>
 */

public interface TaskExecutionOutputWriter {

    void write(ExecutionOutputData output);

}
