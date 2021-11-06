package com.filipovski.gluon.executor.environment.remote;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.filipovski.gluon.executor.configuration.EnvironmentConfigOptions;
import com.filipovski.gluon.executor.environment.runtime.RuntimeEnvironment;
import com.filipovski.gluon.executor.executor.output.ExecutionOutputWriter;
import com.filipovski.gluon.executor.executor.runtime.ExecutorLoader;
import com.filipovski.gluon.executor.executor.runtime.RuntimeExecutorManager;
import com.filipovski.gluon.executor.plugin.DirectoryPluginFinder;
import com.filipovski.gluon.executor.plugin.PluginManager;
import com.filipovski.gluon.executor.proto.*;
import com.filipovski.gluon.executor.task.Task;
import com.filipovski.gluon.executor.task.TaskExecutionActions;
import com.filipovski.gluon.executor.task.TaskFactory;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptor;
import com.filipovski.gluon.executor.task.descriptors.TaskDescriptorFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.util.Map.entry;

/**
 * Executable entry point for a runtime environment.
 *
 * <p>This class facilitates communication between the environment and its
 * remote counterpart by providing RPC endpoints.</p>
 */

// TODO: Replace argument parsing with system properties configuration. Typesafe config
//       supports system properties and provides the ability to override file configuration
//       with commandline-supplied system properties.

public class ExecutionEnvironmentDriver
        extends EnvironmentRuntimeDriverServiceGrpc.EnvironmentRuntimeDriverServiceImplBase {

    private final Logger logger = LogManager.getLogger(EnvironmentDriverServer.class);

    private static final String ENV_CONFIG_FILE_NAME = "environment.conf";

    private final String environmentId;

    private final Config configuration;

    private final EnvironmentDriverServer server;

    private final RemoteEnvironmentEventClient client;

    private final ExecutorLoader executorLoader;

    private final RuntimeEnvironment runtimeEnvironment;

    private ExecutionEnvironmentDriver(String environmentId, Config configuration) {
        this.environmentId = environmentId;
        this.configuration = configuration;
        this.server = new EnvironmentDriverServer(this, configuration.getInt(EnvironmentConfigOptions.PORT));
        this.client = new RemoteEnvironmentEventClient(
                configuration.getString(EnvironmentConfigOptions.GLUON_SERVER_HOST),
                configuration.getInt(EnvironmentConfigOptions.GLUON_SERVER_PORT)
        );
        this.executorLoader = createExecutorLoader(configuration);
        this.runtimeEnvironment = createRuntimeEnvironment();
    }

    private void start() throws IOException, InterruptedException {
        try {
            this.server.start();
            this.client.initialize();
            this.executorLoader.initialize();
            this.register();
            this.server.awaitTermination();
        } catch (Exception e) {
            logger.warn("Environment driver failed to start, exception: [{}]", e);
            stop();
        }
    }

    private void stop() {
        try {
            this.server.shutdown();
            this.runtimeEnvironment.stop();
        } catch (Exception e) {
            logger.warn("A problem has occurred while stopping the environment [{}] [{}]! Shutting down forcefully!",
                    this.environmentId, e.toString());
            this.server.shutdownNow();
        } finally {
            this.client.notifyEnvironmentStopped(this.environmentId);
            this.client.shutdown();
            logger.info("Environment [{}] driver stopped successfully!", this.environmentId);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        EnvironmentDriverArgs driverArgs = parseArguments(args);
        Config configuration = loadConfiguration(driverArgs);
        ExecutionEnvironmentDriver environmentDriver =
                new ExecutionEnvironmentDriver(driverArgs.environmentId, configuration);
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

    private static Config loadConfiguration(EnvironmentDriverArgs args) {
        Map<String, Object> properties = Map.ofEntries(
                entry(EnvironmentConfigOptions.HOST, args.host),
                entry(EnvironmentConfigOptions.PORT, args.port),
                entry(EnvironmentConfigOptions.GLUON_SERVER_HOST, args.serverHost),
                entry(EnvironmentConfigOptions.GLUON_SERVER_PORT, args.serverPort)
        );
        Config defaultConfiguration = ConfigFactory.load(ExecutionEnvironmentDriver.ENV_CONFIG_FILE_NAME);

        return ConfigFactory.parseMap(properties)
                .withFallback(defaultConfiguration)
                .resolve();
    }

    private ExecutorLoader createExecutorLoader(Config configuration) {
        Path pluginsDirectory = Path.of(configuration.getString(EnvironmentConfigOptions.EXECUTOR_PLUGINS_DIR));
        DirectoryPluginFinder pluginFinder = new DirectoryPluginFinder(pluginsDirectory);
        PluginManager pluginManager = new PluginManager(pluginFinder);

        return new ExecutorLoader(pluginManager);
    }

    private RuntimeEnvironment createRuntimeEnvironment() {
        RuntimeExecutorManager executorManager = new RuntimeExecutorManager(executorLoader);
        TaskExecutionActions taskExecutionActions = new TaskExecutionActions(client);
        ExecutionOutputWriter executionOutputWriter = new ExecutionOutputWriter(client);

        return new RuntimeEnvironment(executorManager, taskExecutionActions, executionOutputWriter);
    }

    @Override
    public void execute(TaskExecutionPayload request, StreamObserver<RemoteExecutionResult> responseObserver) {
        logger.info("Execute([{}])", request);
        Task task = createRuntimeTask(request);
        runtimeEnvironment.execute(task);

        responseObserver.onNext(RemoteExecutionResult.newBuilder().build());
        responseObserver.onCompleted();
    }

    public Task createRuntimeTask(TaskExecutionPayload request) {
        Map<String, String> descriptorProperties = request.getTaskDescriptor().getDescriptorPropertiesMap();
        TaskDescriptor descriptor = TaskDescriptorFactory.create(descriptorProperties);

        return TaskFactory.create(descriptor, runtimeEnvironment);
    }

    public void register() {
        EnvironmentRegistrationDetails registrationDetails = EnvironmentRegistrationDetails.newBuilder()
                .setSessionId(this.environmentId)
                .setEnvironmentId(this.environmentId)
                .setHost(configuration.getString(EnvironmentConfigOptions.HOST))
                .setPort(configuration.getInt(EnvironmentConfigOptions.PORT))
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

    @Override
    public void stop(EnvironmentStopMessage request, StreamObserver<EnvironmentStopStatus> responseObserver) {
        stop();
        responseObserver.onNext(EnvironmentStopStatus.newBuilder().build());
        responseObserver.onCompleted();
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
