package com.filipovski.gluonserver.environment;

import com.filipovski.gluon.executor.environment.ExecutionEnvironment;
import com.filipovski.gluon.executor.resourcemanager.ResourceManager;
import com.filipovski.gluon.executor.resourcemanager.WorkerNode;
import com.filipovski.gluon.executor.resourcemanager.WorkerNodeSpec;
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
    private Map<String, ExecutionEnvironment> environmentSessions;

    private Map<String, EnvironmentRegistration> pendingEnvironmentSessionRegistrations;

    private final ResourceManager resourceManager;

    public EnvironmentManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        environmentSessions = new ConcurrentHashMap<>();
        pendingEnvironmentSessionRegistrations = new ConcurrentHashMap<>();
    }

    public Optional<ExecutionEnvironment> getOrCreateSessionEnvironment(String sessionId) {
        return getSessionEnvironment(sessionId)
                .or(() -> createSessionEnvironment(sessionId));
    }

    private Optional<ExecutionEnvironment> createSessionEnvironment(String sessionId) {
        WorkerNodeSpec workerNodeSpec = new WorkerNodeSpec(sessionId);
        CompletableFuture<WorkerNode> workerNodeFuture = resourceManager.startWorkerNode(workerNodeSpec);
        EnvironmentRegistration environmentRegistration = new EnvironmentRegistration(workerNodeFuture);
        pendingEnvironmentSessionRegistrations.put(sessionId, environmentRegistration);

        workerNodeFuture.whenCompleteAsync((WorkerNode workerNode, Throwable throwable) -> {
            if (throwable != null)
                pendingEnvironmentSessionRegistrations.remove(sessionId);
        });

        return getSessionEnvironment(sessionId);
    }

    private Optional<ExecutionEnvironment> getSessionEnvironment(String sessionId) {
        ExecutionEnvironment environment = environmentSessions.get(sessionId);

        return environment == null ? Optional.empty() : Optional.of(environment);
    }

    public void registerExecutionEnvironment() {
        // TODO: Method to be called when Environment is ready
    }
}
