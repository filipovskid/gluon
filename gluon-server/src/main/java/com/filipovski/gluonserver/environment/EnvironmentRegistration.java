package com.filipovski.gluonserver.environment;

import com.filipovski.gluon.executor.resourcemanager.WorkerAllocationResponse;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class EnvironmentRegistration {

    private final CompletableFuture<WorkerAllocationResponse> workerAllocationFuture;

    private final Instant registrationStartTime;

    public EnvironmentRegistration(CompletableFuture<WorkerAllocationResponse> workerAllocationFuture) {
        this.workerAllocationFuture = workerAllocationFuture;
        this.registrationStartTime = Instant.now();
    }

    public EnvironmentRegistration(CompletableFuture<WorkerAllocationResponse> workerAllocationFuture,
                                   Instant registrationStartTime) {
        this.workerAllocationFuture = workerAllocationFuture;
        this.registrationStartTime = registrationStartTime;
    }
    public CompletableFuture<WorkerAllocationResponse> getWorkerNodeFuture() {
        return workerAllocationFuture;
    }

    public Instant getRegistrationStartTime() {
        return registrationStartTime;
    }
}
