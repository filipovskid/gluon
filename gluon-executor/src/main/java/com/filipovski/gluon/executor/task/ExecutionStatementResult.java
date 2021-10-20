package com.filipovski.gluon.executor.task;

import java.time.Duration;

/**
 * The result of executing an {@link ExecutionStatement} task.
 *
 * <p>This is often a handle to the underlying computation.</p>
 */
// TODO: Introduce and document the different executor output types that could be
//       the result of task's execution.

public class ExecutionStatementResult extends TaskResult {

    private boolean hasErrors;

    private String errorMessages;

    private String output;

    public ExecutionStatementResult(String taskId,
                                    Duration runtimeDuration,
                                    boolean hasFailed,
                                    boolean hasErrors,
                                    String errorMessages,
                                    String output) {
        super(taskId, runtimeDuration, hasFailed);

        this.hasErrors = hasErrors;
        this.errorMessages = errorMessages;
        this.output = output;
    }

}
