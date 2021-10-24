package com.filipovski.gluonserver.environment.remote;

import com.filipovski.gluon.executor.proto.*;
import com.filipovski.gluonserver.environment.EnvironmentManager;
import com.filipovski.gluonserver.task.events.TaskStateChangedEvent;
import io.grpc.stub.StreamObserver;

import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;

// TODO: Dispatch local event on execution output arrival

@GrpcService
public class RemoteEnvironmentEventServer extends EnvironmentEventServiceGrpc.EnvironmentEventServiceImplBase {

    private final Logger logger = LoggerFactory.getLogger(RemoteEnvironmentEventServer.class);

    private final EnvironmentManager environmentManager;

    private final ApplicationEventPublisher publisher;

    public RemoteEnvironmentEventServer(EnvironmentManager environmentManager, ApplicationEventPublisher publisher) {
        this.environmentManager = environmentManager;
        this.publisher = publisher;
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

    @Override
    public void updateTaskState(TaskStateDetails request, StreamObserver<TaskStateUpdateStatus> responseObserver) {
        logger.info("Task execution state update arrived: [{}]", request);

        TaskStateChangedEvent event = TaskStateChangedEvent.from(
                request.getTaskId(),
                request.getStatus().name(),
                request.getResult(),
                Instant.now()
        );
        publisher.publishEvent(event);

        TaskStateUpdateStatus status = TaskStateUpdateStatus.newBuilder()
                .setSuccess(true)
                .build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }

    @Override
    public void sendExecutionOutput(ExecutionOutputEvent request, StreamObserver<ExecutionOutputHandlingStatus> responseObserver) {
        logger.info("Execution output event arrived: [{}]", request);

        ExecutionOutputHandlingStatus status = ExecutionOutputHandlingStatus.newBuilder().build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }
}
