package com.filipovski.gluon.executor.resourcemanager;

import java.util.concurrent.CompletableFuture;

/**
 * {@link ResourceManagerBackend} is responsible for requesting and releasing resources
 * from an external resource manager.
 */

public interface ResourceManagerBackend {

    /**
     * Request resource from an external resource manager.
     *
     * @return CompletableFuture that wraps the worker node information.
     */

    CompletableFuture<? extends WorkerNode> requestResource(EnvironmentDriverSpec environmentDriverSpec);

    /**
     * Release resources to an external resource manager.
     *
     * @param nodeId Represents the ID of the worker node being deallocated.
     * @return CompletableFuture that can be used as a handle for tracking the deallocation
     * status. If releasing resources fails, then the future will complete exceptionally.
     */

    CompletableFuture<Void> releaseResource(String nodeId);

}
