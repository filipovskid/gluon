package com.filipovski.gluon.docker;

import com.filipovski.gluon.docker.configuration.DockerConfigOptions;
import com.typesafe.config.Config;

/**
 * A class providing parameters for creating and starting a Docker container.
 */
// TODO: Include gluon configuration

public class EnvironmentDriverContainerSpec {

    private final Config gluonConfig;

    private final String environmentId;

    private final int environmentPort;

    public EnvironmentDriverContainerSpec(Config gluonConfig, String environmentId, int environmentPort) {
        this.gluonConfig = gluonConfig;
        this.environmentId = environmentId;
        this.environmentPort = environmentPort;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public String getEnvironmentHost() {
        return gluonConfig.getString("docker.engine.host");
    }

    public int getEnvironmentPort() {
        return environmentPort;
    }

    public String getServerHost() {
        return gluonConfig.getString("gluon.server.rpc.host");
    }

    public int getServerPort() {
        return gluonConfig.getInt("gluon.server.rpc.port");
    }

    public String getImage() {
        return gluonConfig.getString(DockerConfigOptions.CONTAINER_IMAGE);
    }
}
