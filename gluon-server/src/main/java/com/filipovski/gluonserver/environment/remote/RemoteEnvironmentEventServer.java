package com.filipovski.gluonserver.environment.remote;

import com.filipovski.gluon.executor.proto.EnvironmentEventServiceGrpc;
import com.filipovski.gluon.executor.proto.EnvironmentRegistrationDetails;
import com.filipovski.gluon.executor.proto.EnvironmentRegistrationStatus;
import com.filipovski.gluonserver.environment.EnvironmentManager;
import io.grpc.stub.StreamObserver;

import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class RemoteEnvironmentEventServer extends EnvironmentEventServiceGrpc.EnvironmentEventServiceImplBase {

    private final EnvironmentManager environmentManager;

    public RemoteEnvironmentEventServer(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @Override
    public void registerEnvironmentDriver(EnvironmentRegistrationDetails request,
                                          StreamObserver<EnvironmentRegistrationStatus> responseObserver) {
        EnvironmentRegistrationRequest registrationRequest =
                new EnvironmentRegistrationRequest(request.getSessionId(), request.getHost(), request.getPort());
        environmentManager.onExecutionEnvironmentRegistration(registrationRequest);

        EnvironmentRegistrationStatus status = EnvironmentRegistrationStatus.newBuilder()
                .setSuccess(true)
                .build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }
}
