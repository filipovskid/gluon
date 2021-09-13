package com.filipovski.gluon.executor.resourcemanager;

import java.util.concurrent.CompletableFuture;

/**
 * An interface for bringing up and deploying {@link com.filipovski.gluon.executor.environment.ExecutionEnvironment}
 * worker nodes.
 *
 * <p>Implementations of this interface represent different strategies for allocating
 * new resources. {@link ResourceManager}s may spin up worker nodes in advance and
 * wait for new requests, allocate resources on demand etc.</p>
 *
 * <p>Implementations of this interface should be independent from the resource management
 * framework they are relying on. Allocating resources using different external resource
 * managers is the responsibility of {@link ResourceManagerBackend}.</p>
 */

public interface ResourceManager {

    /**
     * Start a worker node on allocated resources.
     *
     * @return CompletableFuture that wraps the allocated worker node.
     */
    CompletableFuture<WorkerNode> startWorkerNode(WorkerNodeSpec workerNodeSpec);

    /**
     * Stop a worker node and decide whether resources should be released.
     *
     * @return CompletableFuture that could be used as a handle for monitoring the action
     * status. If stopping the worker fails, the future will complete exceptionally.
     */
    CompletableFuture<Void> stopWorkerNode(String nodeId);

}
