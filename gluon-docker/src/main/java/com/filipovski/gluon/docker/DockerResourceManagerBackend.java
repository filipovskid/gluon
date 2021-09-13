package com.filipovski.gluon.docker;

import com.filipovski.gluon.executor.resourcemanager.EnvironmentDriverSpec;
import com.filipovski.gluon.executor.resourcemanager.ResourceManagerBackend;
import com.filipovski.gluon.executor.resourcemanager.WorkerNode;
import com.filipovski.gluon.executor.util.ConnectivityUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Handle Docker engine connection exceptions

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
    public CompletableFuture<? extends WorkerNode> requestResource(EnvironmentDriverSpec environmentDriverSpec) {
        EnvironmentDriverContainerSpec envDriverContainerSpec =
                createEnvironmentDriverContainerSpec(environmentDriverSpec);
        ConfigurableDockerContainer configuredContainer =
                DockerEnvironmentDriverFactory.configureEnvironmentDriverContainer(client, envDriverContainerSpec);
        CompletableFuture<DockerWorkerNode> resourceRequest = new CompletableFuture<>();

        resourceRequests.put(environmentDriverSpec.getEnvironmentId(), resourceRequest);
        DockerContainer container = client.startContainer(configuredContainer);

        return resourceRequest;
    }

    @Override
    public CompletableFuture<Void> releaseResource(String nodeId) {
        return null;
    }

    private EnvironmentDriverContainerSpec createEnvironmentDriverContainerSpec(
            EnvironmentDriverSpec environmentDriverSpec) {
        int environmentPort = 24040;

        try {
            environmentPort = ConnectivityUtil.findAvailablePort();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new EnvironmentDriverContainerSpec(environmentDriverSpec.getEnvironmentId(), environmentPort);
    }
}
