package com.filipovski.gluon.docker;

import java.util.Arrays;
import java.util.List;

/**
 * A class for configuring an EnvironmentDriver Docker container.
 */
// TODO: Use environment configuration for configuring the container.

public class DockerEnvironmentDriverFactory {

    /**
     * Create a {@link ConfigurableDockerContainer} which will be used as a specification
     * for launching a Docker container. This method should configure everything a container
     * needs to run an EnvironmentDriver process.
     *
     * @return Returns a configured Docker container
     */
    public static ConfigurableDockerContainer configureEnvironmentDriverContainer(
            GluonDockerClient client,
            EnvironmentDriverContainerSpec envDriverContainerSpec) {
        ConfigurableDockerContainer configuredContainer = client.createConfigurableContainer();
        configuredContainer = initEnvironmentConfigurer(configuredContainer, envDriverContainerSpec);
        configuredContainer = paramEnvironmentConfigurer(configuredContainer, envDriverContainerSpec);

        return configuredContainer;
    }

    private static ConfigurableDockerContainer initEnvironmentConfigurer(
            ConfigurableDockerContainer container,
            EnvironmentDriverContainerSpec envDriverContainerSpec) {
        return ConfigurableDockerContainer.from(
                container.getInternalContainer()
                        .withImage(envDriverContainerSpec.getImage())
        );
    }

    private static ConfigurableDockerContainer paramEnvironmentConfigurer(
            ConfigurableDockerContainer container,
            EnvironmentDriverContainerSpec envDriverContainerSpec) {
        List<String> commandParameters = Arrays.asList(
                "--host %s".formatted(envDriverContainerSpec.getEnvironmentHost()),
                "--port %d".formatted(envDriverContainerSpec.getEnvironmentPort()),
                "--server-host %s".formatted(envDriverContainerSpec.getServerHost()),
                "--server-port %d".formatted(envDriverContainerSpec.getServerPort()),
                "--env-id %s".formatted(envDriverContainerSpec.getEnvironmentId()),
                "--worker-id \"test_driver\""
        );

        return ConfigurableDockerContainer.from(
                container.getInternalContainer()
                        .withCmd(commandParameters)
        );

    }
}
