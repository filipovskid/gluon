package com.filipovski.gluon.executor.executor;

/**
 * Class containing the data needed for an {@link Executor} to execute some code.
 *
 * <p>Current executors execute a code without the need of additional information,
 * thus this implementation contains only the code. However, if new requirements
 * arise this class should enable them.</p>
 */

public class ExecutionData {

    private final String code;

    public ExecutionData(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
