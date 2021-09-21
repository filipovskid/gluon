package com.filipovski.gluon.executor.environment.remote;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.filipovski.gluon.executor.proto.EnvironmentRuntimeDriverServiceGrpc;
import com.filipovski.gluon.executor.proto.ExecutionPayload;
import com.filipovski.gluon.executor.proto.RemoteExecutionResult;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Executable entry point for a runtime environment.
 *
 * <p>This class facilitates communication between the environment and its
 * remote counterpart by providing RPC endpoints.</p>
 */

public class ExecutionEnvironmentDriver
        extends EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceImplBase {

    private final Logger logger = LoggerFactory.getLogger(ExecutionEnvironmentDriver.class);

    private final String driverHost;

    private final int driverPort;

    private final String gluonServerHost;

    private final int gluonServerPort;

    private final EnvironmentDriverServer server;

    private ExecutionEnvironmentDriver(String host, int port, String serverHost, int serverPort) {
        driverHost = host;
        driverPort = port;
        gluonServerHost = serverHost;
        gluonServerPort = serverPort;
        server = new EnvironmentDriverServer(this, port);
    }

    private void start() throws IOException, InterruptedException {
        this.server.start();
        this.server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        EnvironmentDriverArgs driverArgs = parseArguments(args);
        ExecutionEnvironmentDriver environmentDriver =
                new ExecutionEnvironmentDriver(driverArgs.host,
                        driverArgs.port,
                    driverArgs.serverHost,
                    driverArgs.serverPort
                );
        environmentDriver.start();
    }

    private static EnvironmentDriverArgs parseArguments(String[] args) {
        EnvironmentDriverArgs driverArgs = new EnvironmentDriverArgs();
        JCommander.newBuilder()
                .addObject(driverArgs)
                .build()
                .parse(args);

        return driverArgs;
    }

    @Override
    public void execute(ExecutionPayload request, StreamObserver<RemoteExecutionResult> responseObserver) {
        logger.info("Execute([{}])", request);

        responseObserver.onNext(RemoteExecutionResult.newBuilder().build());
        responseObserver.onCompleted();
    }

    public static class EnvironmentDriverArgs {
        @Parameter(names = { "--host", "-h" }, description = "Environment driver hostname")
        public String host;

        @Parameter(names = { "--port", "-p" }, description = "Environment driver port")
        public Integer port;

        @Parameter(names = { "--server-host", "-r" }, description = "Gluon server hostname")
        public String serverHost;

        @Parameter(names = { "--server-port", "-d" }, description = "Gluon server port")
        public int serverPort;

        @Parameter(names = "--env-id", description = "Environment identifier")
        public String environmentId;

        @Parameter(names = "--worker-id", description = "Worker node identifier")
        public String workerId;
    }
}
