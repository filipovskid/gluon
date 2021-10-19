package com.filipovski.gluon.executor.environment.remote;

import com.filipovski.gluon.executor.proto.*;
import com.filipovski.gluon.executor.proto.EnvironmentEventServiceGrpc.EnvironmentEventServiceBlockingStub;
import com.filipovski.gluon.executor.proto.EnvironmentEventServiceGrpc.EnvironmentEventServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RemoteEnvironmentEventClient {

    private final String host;

    private final int port;

    private boolean isInitialized;

    private ManagedChannel channel;

    private EnvironmentEventServiceBlockingStub blockingStub;

    private EnvironmentEventServiceStub asyncStub;

    public RemoteEnvironmentEventClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.isInitialized = false;
    }

    public void initialize() {
        if(isInitialized)
            return;

        isInitialized = true;
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext();
        channel = channelBuilder.build();
        blockingStub = EnvironmentEventServiceGrpc.newBlockingStub(channel);
        asyncStub = EnvironmentEventServiceGrpc.newStub(channel);
    }

    public EnvironmentRegistrationStatus registerEnvironmentDriver(EnvironmentRegistrationDetails registrationDetails) {
        return this.blockingStub.registerEnvironmentDriver(registrationDetails);
    }

    public TaskStateUpdateStatus updateTaskStatus(TaskStateDetails details) {
        return this.blockingStub.updateTaskState(details);
    }
}
