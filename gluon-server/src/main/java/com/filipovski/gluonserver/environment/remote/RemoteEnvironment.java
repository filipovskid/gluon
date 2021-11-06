package com.filipovski.gluonserver.environment.remote;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.proto.EnvironmentStopMessage;
import com.filipovski.gluon.executor.proto.TaskDescriptionData;
import com.filipovski.gluon.executor.proto.TaskExecutionPayload;
import com.filipovski.gluon.executor.task.Task;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptor;

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
    public void start() {
        client.initialize();
    }

    @Override
    public void execute(Task task) {
        this.client.callFunctionBlocking(client -> {
            TaskExecutionPayload payload = createTaskPayload(task);
            return client.execute(payload);
        });
    }

    @Override
    public void stop() {
        EnvironmentStopMessage message = EnvironmentStopMessage.newBuilder().build();
        this.client.callFunctionBlocking(client -> client.stop(message));
        this.client.shutdown();
    }

    private TaskExecutionPayload createTaskPayload(Task task) {
        TaskDescriptor taskDescriptor = task.getTaskDescriptor();
        TaskDescriptionData taskDescription = TaskDescriptionData.newBuilder()
                .setTaskClassName(taskDescriptor.getTaskClassName())
                .putAllDescriptorProperties(taskDescriptor.toDescriptorMap())
                .build();

        return TaskExecutionPayload.newBuilder()
                .setTaskId(task.getId())
                .setTaskDescriptor(taskDescription)
                .build();
    }
}