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
    public static ConfigurableDockerContainer configureEnvironmentDriverContainer(GluonDockerClient client) {
        ConfigurableDockerContainer configuredContainer = client.createConfigurableContainer();
        configuredContainer = initEnvironmentConfigurer(configuredContainer);
        configuredContainer = paramEnvironmentConfigurer(configuredContainer);

        return configuredContainer;
    }

    private static ConfigurableDockerContainer initEnvironmentConfigurer(ConfigurableDockerContainer container) {
        return ConfigurableDockerContainer.from(
                container.getInternalContainer()
                .withImage("gluon-executor:0.0.1")
        );
    }

    private static ConfigurableDockerContainer paramEnvironmentConfigurer(ConfigurableDockerContainer container) {
        List<String> commandParameters = Arrays.asList(
                "--host 192.168.1.100",
                "--port 24090",
                "--server-host 192.168.1.100",
                "--server-port 8082",
                "--env-id \"test_environment\"",
                "--worker-id \"test_driver\""
        );

        return ConfigurableDockerContainer.from(
                container.getInternalContainer()
                .withCmd(commandParameters)
        );

//        --host 192.168.1.100 --port 24090 --server-host 192.168.1.100 --server-port 8082 --env-id "test_environment" --worker-id "test_driver"
    }
}
