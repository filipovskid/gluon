package com.filipovski.gluon.docker;

import com.filipovski.gluon.executor.resourcemanager.ResourceManagerBackend;
import com.filipovski.gluon.executor.resourcemanager.WorkerNode;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DockerResourceManagerBackend implements ResourceManagerBackend {

    private final GluonDockerClient client;

    /**
     * Resource requests for ongoing resource allocations.
     */
    private Map<String, CompletableFuture<DockerWorkerNode>> resourceRequests;

    private final Random random = new Random();

    public DockerResourceManagerBackend(GluonDockerClient client) {
        this.client = client;
        this.resourceRequests = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<? extends WorkerNode> requestResource() {
        ConfigurableDockerContainer configuredContainer =
                DockerEnvironmentDriverFactory.configureEnvironmentDriverContainer(client);
        CompletableFuture<DockerWorkerNode> resourceRequest = new CompletableFuture<>();
        String environmentId = "test-" + this.random.nextInt();

        resourceRequests.put(environmentId, resourceRequest);

        DockerContainer container = client.startContainer(configuredContainer);

        return resourceRequest;
    }

    @Override
    public CompletableFuture<Void> releaseResource(String nodeId) {
        return null;
    }
}
