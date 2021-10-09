package com.filipovski.gluon.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * A factory for creating the {@link GluonDockerClient}
 */
// TODO: Make the client configurable via a configuration. The configuration
//       should be passed as an argument.

public class GluonDockerClientFactory {

    /**
     * Create a Gluon Docker client.
     *
     * @return Returns a configured Gluon Docker client
     */
    public static GluonDockerClient createDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        DockerClient client = DockerClientBuilder.getInstance(config).build();

        return new GluonDockerClient(client);
    }
}
