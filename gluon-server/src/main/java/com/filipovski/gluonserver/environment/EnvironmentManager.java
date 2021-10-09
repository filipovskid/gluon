package com.filipovski.gluonserver.environment;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.resourcemanager.ResourceManager;
import com.filipovski.gluon.executor.resourcemanager.WorkerAllocationResponse;
import com.filipovski.gluon.executor.resourcemanager.WorkerNodeSpec;
import com.filipovski.gluonserver.environment.remote.EnvironmentRegistrationRequest;
import com.filipovski.gluonserver.environment.remote.RemoteEnvironment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is where {@link ExecutionEnvironment}s are created and associated with
 * a given session.
 */
// TODO: Worker specification should be configured with an environment id instead
//       of a session id

@Service
public class EnvironmentManager {

    // Mappings sessionId -> execution environment. This might be subject to a change
    // by introducing some sort of state/session and a corresponding store.
    private Map<String, ExecutionEnvironment> sessionEnvironments;

    private Map<String, EnvironmentRegistration> pendingEnvironmentSessionRegistrations;

    private final ResourceManager resourceManager;

    public EnvironmentManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        sessionEnvironments = new ConcurrentHashMap<>();
        pendingEnvironmentSessionRegistrations = new ConcurrentHashMap<>();
    }

    public void createSessionEnvironment(String sessionId) {
        WorkerNodeSpec workerNodeSpec = new WorkerNodeSpec(sessionId);

        CompletableFuture<WorkerAllocationResponse> workerRequestFuture =
                resourceManager.startWorkerNode(workerNodeSpec);

        EnvironmentRegistration environmentRegistration = new EnvironmentRegistration(workerRequestFuture);
        pendingEnvironmentSessionRegistrations.put(sessionId, environmentRegistration);

        workerRequestFuture.handle((allocationResponse, throwable) -> {
            if (throwable != null) {
                pendingEnvironmentSessionRegistrations.remove(sessionId);
            }
            return null;
        });
    }

    public Optional<ExecutionEnvironment> getSessionEnvironment(String sessionId) {
        ExecutionEnvironment environment = sessionEnvironments.get(sessionId);

        return environment == null ? Optional.empty() : Optional.of(environment);
    }

    public void onExecutionEnvironmentRegistration(EnvironmentRegistrationRequest registrationRequest) {
        sessionEnvironments.computeIfAbsent(registrationRequest.getSessionId(), (key) -> {
            RemoteEnvironment remoteEnvironment = new RemoteEnvironment(registrationRequest.getHost(),
                    registrationRequest.getPort());
            remoteEnvironment.open();

            return remoteEnvironment;
        });
    }
}
