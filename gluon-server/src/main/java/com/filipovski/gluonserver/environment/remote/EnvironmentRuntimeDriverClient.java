package com.filipovski.gluonserver.environment.remote;

import com.filipovski.gluon.executor.proto.EnvironmentRuntimeDriverServiceGrpc;
import com.filipovski.gluon.executor.proto.EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceStub;
import com.filipovski.gluon.executor.proto.EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.function.Function;

/**
 * gRPC client class used to enable communication with remote ExecutionEnvironmentDriver.
 * The class provides methods that enable calling functions on both blocking and asynchronous stubs.
 */

public class EnvironmentRuntimeDriverClient {

    private final String host;

    private final int port;

    private ManagedChannel channel;

    private EnvironmentRuntimeDriverServiceBlockingStub blockingStub;

    private EnvironmentRuntimeDriverServiceStub asyncStub;

    public EnvironmentRuntimeDriverClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initialize() {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext();
        this.channel = channelBuilder.build();
        this.blockingStub = EnvironmentRuntimeDriverServiceGrpc.newBlockingStub(this.channel);
        this.asyncStub = EnvironmentRuntimeDriverServiceGrpc.newStub(this.channel);
    }

    public <R> R callFunctionBlocking(Function<EnvironmentRuntimeDriverServiceBlockingStub, R> function) {
        return function.apply(this.blockingStub);
    }

    public <R> R callFunctionAsync(Function<EnvironmentRuntimeDriverServiceStub, R> function) {
        return function.apply(this.asyncStub);
    }
}
