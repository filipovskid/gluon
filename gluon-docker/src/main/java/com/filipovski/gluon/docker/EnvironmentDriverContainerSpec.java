package com.filipovski.gluon.docker;

/**
 * A class providing parameters for creating and starting a Docker container.
 */
// TODO: Include gluon configuration

public class EnvironmentDriverContainerSpec {

    private final String environmentId;

    private final int environmentPort;

    public EnvironmentDriverContainerSpec(String environmentId, int environmentPort) {
        this.environmentId = environmentId;
        this.environmentPort = environmentPort;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public String getEnvironmentHost() {
        return "192.168.1.100";
    }

    public int getEnvironmentPort() {
        return environmentPort;
    }

    public String getServerHost() {
        return "192.168.1.100";
    }

    public int getServerPort() {
        return 9090;
    }

    public String getImage() {
        return "gluon-executor:0.0.1";
    }
}
