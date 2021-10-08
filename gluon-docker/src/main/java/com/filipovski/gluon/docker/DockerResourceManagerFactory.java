package com.filipovski.gluon.docker;

import com.typesafe.config.Config;

public class DockerResourceManagerFactory {

    public static DockerResourceManagerBackend createResourceManagerBackend(Config configuration) {
        GluonDockerClient dockerClient = GluonDockerClientFactory.createDockerClient();

        return new DockerResourceManagerBackend(configuration, dockerClient);
    }
}
