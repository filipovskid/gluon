package com.filipovski.gluonserver.environment.remote;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.proto.ExecutionPayload;
import com.filipovski.gluon.executor.task.Task;

/**
 * An {@link ExecutionEnvironment} that acts as a proxy for an environment
 * that runs on a remote WorkerNode. The environment needs to be created
 * with the host and port information of the ExecutionEnvironmentDriver
 * which represents the corresponding RuntimeEnvironment.
 */

public class RemoteEnvironment implements ExecutionEnvironment {

    private final EnvironmentRuntimeDriverClient client;

    public RemoteEnvironment(String host, int port) {
        client = new EnvironmentRuntimeDriverClient(host, port);
    }

    @Override
    public void open() {
        client.initialize();
    }

    @Override
    public void execute(Task task) {
        this.client.callFunctionBlocking(client -> {
            ExecutionPayload payload = ExecutionPayload.newBuilder().build();
            return client.execute(payload);
        });
    }

    @Override
    public void close() {

    }
}