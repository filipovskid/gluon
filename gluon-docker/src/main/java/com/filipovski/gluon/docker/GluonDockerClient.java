package com.filipovski.gluon.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;

/**
 * A client for communicating with Docker engine. The client provides operations
 * for configuring and managing docker containers. These operations might evaluate
 * to several actions on the Docker engine.
 *
 * <p>Long-running operations should be implemented asynchronously using Futures.</p>
 */

public class GluonDockerClient {

    private final DockerClient client;

    public GluonDockerClient(DockerClient client) {
        this.client = client;
    }

    /**
     * Create non-configured configurable container. This acts as an empty
     * specification for a docker container.
     */
    public ConfigurableDockerContainer createConfigurableContainer() {
        return ConfigurableDockerContainer.from(client.createContainerCmd(""));
    }

    /** Start a container.
     *
     * @param configurableContainer Configured container from which a docker container is started
     * @return Returns information for the started container
     */
    // TODO: startContainerCmd is not asynchronous. Check whether it is slow.
    public DockerContainer startContainer(ConfigurableDockerContainer configurableContainer) {
        CreateContainerResponse preparedContainer = configurableContainer.getInternalContainer().exec();
        client.startContainerCmd(preparedContainer.getId()).exec();

        return DockerContainer.newBuilder()
                .withId(preparedContainer.getId())
                .build();
    }
}
