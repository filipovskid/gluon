package com.filipovski.gluon.executor.environment.remote;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.filipovski.gluon.executor.proto.*;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

/**
 * Executable entry point for a runtime environment.
 *
 * <p>This class facilitates communication between the environment and its
 * remote counterpart by providing RPC endpoints.</p>
 */

public class ExecutionEnvironmentDriver
        extends EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceImplBase {

    private final Logger logger = LogManager.getLogger(EnvironmentDriverServer.class);

    private final String environmentId;

    private final String driverHost;

    private final int driverPort;

    private final String gluonServerHost;

    private final int gluonServerPort;

    private final EnvironmentDriverServer server;

    private final RemoteEnvironmentEventClient client;

    private ExecutionEnvironmentDriver(String environmentId, String host, int port, String serverHost, int serverPort) {
        this.environmentId = environmentId;
        driverHost = host;
        driverPort = port;
        gluonServerHost = serverHost;
        gluonServerPort = serverPort;
        server = new EnvironmentDriverServer(this, port);
        client = new RemoteEnvironmentEventClient(serverHost, serverPort);
    }

    private void start() throws IOException, InterruptedException {
        try {
            this.server.start();
            this.client.initialize();
            this.register();
            this.server.awaitTermination();
        } catch (Exception e) {
            logger.warn("Environment driver failed to start, exception: [{}]", e);
            stop();
        }
    }

    private void stop() {
        this.server.shutdown();
        logger.info("Environment driver stopped successfully!");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        EnvironmentDriverArgs driverArgs = parseArguments(args);
        ExecutionEnvironmentDriver environmentDriver =
                new ExecutionEnvironmentDriver(driverArgs.environmentId,
                        driverArgs.host,
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

    public void register() {
        EnvironmentRegistrationDetails registrationDetails = EnvironmentRegistrationDetails.newBuilder()
                .setSessionId(this.environmentId)
                .setEnvironmentId(this.environmentId)
                .setHost(this.driverHost)
                .setPort(this.driverPort)
                .build();

        try {
            EnvironmentRegistrationStatus status = client.registerEnvironmentDriver(registrationDetails);
            if (status.getSuccess()) {
                logger.info("Registration completed successfully!");
            } else {
                logger.warn("Registration failed!");
                stop();
            }
        } catch (Exception e) {
            logger.warn("Exception occurred while registering environment driver: [{}], exception:",
                    registrationDetails, e);
            stop();
        }
    }

    public static class EnvironmentDriverArgs {
        @Parameter(names = {"--host", "-h"}, description = "Environment driver hostname")
        public String host;

        @Parameter(names = {"--port", "-p"}, description = "Environment driver port")
        public Integer port;

        @Parameter(names = {"--server-host", "-r"}, description = "Gluon server hostname")
        public String serverHost;

        @Parameter(names = {"--server-port", "-d"}, description = "Gluon server port")
        public int serverPort;

        @Parameter(names = "--env-id", description = "Environment identifier")
        public String environmentId;

        @Parameter(names = "--worker-id", description = "Worker node identifier")
        public String workerId;
    }
}
