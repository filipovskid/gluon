package com.filipovski.gluon.docker;

import com.google.common.base.Strings;

/**
 * Represents a container managed by docker engine.
 *
 * <p>This class contains the data which enables container management
 * through {@link com.github.dockerjava.api.DockerClient}.</p>
 */

public class DockerContainer {

    private final String id;

    private DockerContainer(String id) {
        if(Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("Container id must not be null");

        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String id;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public DockerContainer build() {
            return new DockerContainer(this.id);
        }
    }
}
