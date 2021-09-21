package com.filipovski.gluon.executor.environment.remote;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Class representing a server listening for and dispatching incoming calls to
 * {@link com.filipovski.gluon.executor.proto.EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceImplBase}
 * implementations.
 *
 * <p>Note: This class might be extracted to a separate RPC server utility or
 * completely replaced by an inplace server construction.</p>
 */

public class EnvironmentDriverServer {

    private final Logger logger = LogManager.getLogger(EnvironmentDriverServer.class);

    private final int port;

    private final Server server;

    public EnvironmentDriverServer(BindableService bindableService, int port) {
        this(ServerBuilder.forPort(port), bindableService,  port);
    }

    public EnvironmentDriverServer(ServerBuilder<?> serverBuilder,
                                   BindableService bindableService,
                                   int port) {
        this.port = port;
        this.server = serverBuilder
                .addService(bindableService)
                .addService(ProtoReflectionService.newInstance())
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Driver server started listening on port [{}]", port);
    }

    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return server.awaitTermination(timeout, unit);
    }

    public int getPort() {
        return server.getPort();
    }

    public boolean isShutdown() {
        return server.isShutdown();
    }

    public boolean isTerminated() {
        return server.isTerminated();
    }

    public void shutdown() {
        server.shutdown();
    }

    public void shutdownNow() {
        server.shutdownNow();
    }
}
