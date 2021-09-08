package com.filipovski.gluonserver.environment;

import com.filipovski.gluon.executor.resourcemanager.WorkerNode;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class EnvironmentRegistration {
    private final CompletableFuture<WorkerNode> workerNodeFuture;

    private final Instant registrationStartTime;

    public EnvironmentRegistration(CompletableFuture<WorkerNode> workerNodeFuture) {
        this.workerNodeFuture = workerNodeFuture;
        this.registrationStartTime = Instant.now();
    }

    public EnvironmentRegistration(CompletableFuture<WorkerNode> workerNodeFuture, Instant registrationStartTime) {
        this.workerNodeFuture = workerNodeFuture;
        this.registrationStartTime = registrationStartTime;
    }
    public CompletableFuture<WorkerNode> getWorkerNodeFuture() {
        return workerNodeFuture;
    }

    public Instant getRegistrationStartTime() {
        return registrationStartTime;
    }
}
