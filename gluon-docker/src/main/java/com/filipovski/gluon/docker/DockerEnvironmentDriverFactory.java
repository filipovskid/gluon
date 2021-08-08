package com.filipovski.gluon.docker;

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

        return configuredContainer;
    }

    private static ConfigurableDockerContainer initEnvironmentConfigurer(ConfigurableDockerContainer container) {
        return ConfigurableDockerContainer.from(
                container.getInternalContainer()
                .withImage("hello-world")
        );
    }
}
