package com.filipovski.gluonserver.resourcemanager;

import com.filipovski.gluon.executor.resourcemanager.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Resource manager for allocating and deallocating resources on request.
 *
 * <p>This class can request resources from various external resource management frameworks
 * enabled by supplying a specific implementation of {@link ResourceManagerBackend}.
 * </p>
 */

@Service
public class ReactiveResourceManager implements ResourceManager {

    private final ResourceManagerBackend resourceManagerBackend;

    public ReactiveResourceManager(ResourceManagerBackend resourceManagerBackend) {
        this.resourceManagerBackend = resourceManagerBackend;
    }

    @Override
    public CompletableFuture<WorkerNode> startWorkerNode(WorkerNodeSpec workerNodeSpec) {
        return resourceManagerBackend.requestResource(new EnvironmentDriverSpec(workerNodeSpec.getOwnerId()))
                .thenApply(workerNode -> (WorkerNode) workerNode);
    }

    @Override
    public CompletableFuture<Void> stopWorkerNode(String nodeId) {
        return resourceManagerBackend.releaseResource(nodeId);
    }
}
