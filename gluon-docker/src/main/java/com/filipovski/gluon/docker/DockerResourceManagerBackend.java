package com.filipovski.gluon.docker;

import com.filipovski.gluon.executor.resourcemanager.ResourceManagerBackend;
import com.filipovski.gluon.executor.resourcemanager.WorkerNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DockerResourceManagerBackend implements ResourceManagerBackend {

    private final DockerClient client;

    /**
     * Resource requests for ongoing resource allocations.
     */
    private Map<String, CompletableFuture<DockerWorkerNode>> resourceRequests;

    public DockerResourceManagerBackend() {
        this.client = DockerClientBuilder.getInstance().build();
    }

    @Override
    public CompletableFuture<? extends WorkerNode> requestResource() {
        CompletableFuture<DockerWorkerNode> resourceRequest = new CompletableFuture<>();

        client.pullImageCmd("hello-world")
                .exec(new PullImageResultCallback());

        return resourceRequest;
    }

    @Override
    public CompletableFuture<Void> releaseResource(String nodeId) {
        return null;
    }

    private final class PullImageResultCallback implements ResultCallback<PullResponseItem> {

        @Override
        public void onStart(Closeable closeable) {

        }

        @Override
        public void onNext(PullResponseItem pullResponseItem) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {
            CreateContainerResponse container = client.createContainerCmd("hello-world").exec();
            client.startContainerCmd(container.getId()).exec();
        }


        @Override
        public void close() throws IOException {

        }
    }
