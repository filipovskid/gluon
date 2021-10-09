package com.filipovski.gluonserver.resourcemanager;

import com.filipovski.gluon.executor.resourcemanager.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, WorkerNode> workerNodes;

    public ReactiveResourceManager(ResourceManagerBackend resourceManagerBackend) {
        this.resourceManagerBackend = resourceManagerBackend;
        this.workerNodes = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<WorkerAllocationResponse> startWorkerNode(WorkerNodeSpec workerNodeSpec) {

        CompletableFuture<WorkerNode> resourceRequestFuture = resourceManagerBackend
                .requestResource(new EnvironmentDriverSpec(workerNodeSpec.getOwnerId()))
                .thenApply(workerNode -> (WorkerNode) workerNode);

        return resourceRequestFuture.handle(
                (workerNode, throwable) -> {
                    if(throwable != null)
                        return WorkerAllocationResponse.createFailedResponse("Worker allocation failed!");

                    workerNodes.put(workerNode.getNodeId(), workerNode);
                    return WorkerAllocationResponse.createSuccessfulResponse();
                }
        );
    }

    @Override
    public CompletableFuture<Void> stopWorkerNode(String nodeId) {
        return resourceManagerBackend.releaseResource(nodeId);
    }
}
