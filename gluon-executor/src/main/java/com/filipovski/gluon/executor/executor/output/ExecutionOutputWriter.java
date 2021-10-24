package com.filipovski.gluon.executor.executor.output;

import com.filipovski.gluon.executor.environment.remote.RemoteEnvironmentEventClient;
import com.filipovski.gluon.executor.proto.ExecutionOutputEvent;

/**
 * {@link ExecutionOutputWriter} sends execution output to a remote server. A single
 * instance of this class should be used within the entire environment (runtime).
 *
 * <p>The data that is being written encapsulates both the execution output data
 * and task specific information.</p>
 */

public class ExecutionOutputWriter {

    private final RemoteEnvironmentEventClient client;

    public ExecutionOutputWriter(RemoteEnvironmentEventClient client) {
        this.client = client;
    }

    public void write(ExecutionOutput output) {
        ExecutionOutputEvent event = ExecutionOutputEvent.newBuilder()
                .setTaskId(output.getTaskId())
                .setData(output.getData().serialize())
                .build();
        client.sendExecutionOutput(event);
    }
}
