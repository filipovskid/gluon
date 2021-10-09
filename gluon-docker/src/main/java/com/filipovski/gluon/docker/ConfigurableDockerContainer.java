package com.filipovski.gluon.docker;

import com.github.dockerjava.api.command.CreateContainerCmd;

import java.util.Objects;

/**
 * {@link ConfigurableDockerContainer} represents a container specification
 * which is then used as a configuration for creating an actual docker container.
 */

public class ConfigurableDockerContainer {

    private final CreateContainerCmd configurableContainer;

    private ConfigurableDockerContainer(CreateContainerCmd configurableContainer) {
        this.configurableContainer = configurableContainer;
    }

    public static ConfigurableDockerContainer from(CreateContainerCmd createContainerCmd) {
        CreateContainerCmd containerComposer = Objects.requireNonNull(createContainerCmd);
        return new ConfigurableDockerContainer(containerComposer);
    }

    public CreateContainerCmd getInternalContainer() {
        return configurableContainer;
    }
}
