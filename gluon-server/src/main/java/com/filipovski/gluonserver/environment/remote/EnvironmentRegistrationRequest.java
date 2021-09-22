package com.filipovski.gluonserver.environment.remote;

/**
 * Request containing details necessary to register a {@link RemoteEnvironment}.
 */

public class EnvironmentRegistrationRequest {

    private final String sessionId;

    private final String host;

    private final int port;

    public EnvironmentRegistrationRequest(String sessionId, String host, int port) {
        this.sessionId = sessionId;
        this.host = host;
        this.port = port;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
