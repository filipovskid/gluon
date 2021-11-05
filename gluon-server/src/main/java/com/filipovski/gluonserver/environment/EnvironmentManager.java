package com.filipovski.gluonserver.environment;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.resourcemanager.ResourceManager;
import com.filipovski.gluon.executor.resourcemanager.WorkerAllocationResponse;
import com.filipovski.gluon.executor.resourcemanager.WorkerNodeSpec;
import com.filipovski.gluonserver.environment.events.EnvironmentStatusChangedEvent;
import com.filipovski.gluonserver.environment.remote.EnvironmentRegistrationRequest;
import com.filipovski.gluonserver.environment.remote.RemoteEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is where {@link ExecutionEnvironment}s are created and associated with
 * a given session.
 */
// TODO: Worker specification should be configured with an environment id instead
//       of a session id

@Service
public class EnvironmentManager {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentManager.class);

    private final ResourceManager resourceManager;

    private final ApplicationEventPublisher publisher;

    // Mappings sessionId -> execution environment. This might be subject to a change
    // by introducing some sort of state/session and a corresponding store.
    private ConcurrentMap<String, ExecutionEnvironment> sessionEnvironments;

    private ConcurrentMap<String, EnvironmentRegistration> pendingEnvironmentSessionRegistrations;



    public EnvironmentManager(ResourceManager resourceManager,
                              ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.resourceManager = resourceManager;
        this.sessionEnvironments = new ConcurrentHashMap<>();
        this.pendingEnvironmentSessionRegistrations = new ConcurrentHashMap<>();
    }

    public void createSessionEnvironment(String sessionId) {
        WorkerNodeSpec workerNodeSpec = new WorkerNodeSpec(sessionId);

        pendingEnvironmentSessionRegistrations.computeIfAbsent(sessionId, sessionIdKey -> {
            if (sessionEnvironments.containsKey(sessionId)) {
                updateEnvironmentStatus(
                        sessionId,
                        EnvironmentStatus.STARTED,
                        "Environment has been already been started!"
                );
                return null;
            }

            CompletableFuture<WorkerAllocationResponse> workerRequestFuture =
                    resourceManager.startWorkerNode(workerNodeSpec);

            workerRequestFuture.handle((allocationResponse, throwable) -> {
                if (throwable != null || !allocationResponse.isSuccessful()) {
                    logger.warn("Environment [{}] resource allocation failed. [{}] [{}]",
                            sessionIdKey, allocationResponse,  throwable);
                    pendingEnvironmentSessionRegistrations.remove(sessionId);
                    updateEnvironmentStatus(
                            sessionIdKey,
                            EnvironmentStatus.FAILED,
                            "Environment resource allocation failed!"
                    );
                }

                logger.info("Environment [{}] resource allocation has completed successfully. [{}]",
                        sessionIdKey, allocationResponse);
                updateEnvironmentStatus(
                        sessionIdKey,
                        EnvironmentStatus.STARTING,
                        "Environment resource allocation has completed successfully!"
                );

                return null;
            });

            return new EnvironmentRegistration(workerRequestFuture);
        });
    }

    public Optional<ExecutionEnvironment> getSessionEnvironment(String sessionId) {
        ExecutionEnvironment environment = sessionEnvironments.get(sessionId);

        return environment == null ? Optional.empty() : Optional.of(environment);
    }

    public void onExecutionEnvironmentRegistration(EnvironmentRegistrationRequest registrationRequest) {
        sessionEnvironments.computeIfAbsent(registrationRequest.getSessionId(), (sessionId) -> {
            pendingEnvironmentSessionRegistrations.remove(registrationRequest.getSessionId());
            RemoteEnvironment remoteEnvironment = new RemoteEnvironment(registrationRequest.getHost(),
                    registrationRequest.getPort());
            remoteEnvironment.start();

            logger.info("Environment [{}] has been successfully started and registered.", sessionId);
            updateEnvironmentStatus(
                    sessionId,
                    EnvironmentStatus.STARTED,
                    "Environment has been successfully started and registered!"
            );

            return remoteEnvironment;
        });
    }

    private void updateEnvironmentStatus(String sessionId, EnvironmentStatus status) {
        this.updateEnvironmentStatus(sessionId, status, "Empty message.");
    }

    private void updateEnvironmentStatus(String sessionId, EnvironmentStatus status, String message) {
        EnvironmentStatusChangedEvent event =
                EnvironmentStatusChangedEvent.from(sessionId, status, message, Instant.now());
        publisher.publishEvent(event);
    }
}
