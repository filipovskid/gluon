package com.filipovski.gluon.executor.resourcemanager;

/**
 * Specification used to request a resource from a {@link ResourceManagerBackend}.
 *
 * <p>The class contains configuration information relevant for the identification of the
 * {@link com.filipovski.gluon.executor.environment.remote.ExecutionEnvironmentDriver}.</p>
 *
 * <p>Future changes might introduce additional resource specifications for describing different
 * resource dimensions.</p>
 */

public class EnvironmentDriverSpec {

    private final String environmentId;

    public EnvironmentDriverSpec(String environmentId) {
        this.environmentId = environmentId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }
}
