package com.filipovski.gluon.executor.resourcemanager;

/**
 * Worker specification used for requesting new worker from {@link ResourceManager}.
 *
 * <p>This class contains information relevant for the identification of the worker process.
 * Future changes can introduce information describing different resource dimensions of the
 * worker node.</p>
 */

public class WorkerNodeSpec {

    private final String ownerId;

    public WorkerNodeSpec(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
