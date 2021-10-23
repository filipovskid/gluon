package com.filipovski.gluon.executor.executor.output;

import com.filipovski.gluon.executor.util.JsonSerializable;

/**
 * Type representing the data received as execution output.
 *
 * <p>Implementations of this interface can take any form with the constraint
 * of being JSON serializable.</p>
 */

public interface ExecutionOutputData extends JsonSerializable { }
