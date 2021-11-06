package com.filipovski.gluonserver.environment;

/**
 * Environment status used for notifying client services about environment's status.
 *
 * <p>Currently is being used to notify services on the progress of environment
 * resource allocation, startup and termination. Future improvements should track
 * failures.</p>
 */

public enum EnvironmentStatus {
    /**
     * Environment resources have been allocated and the environment is being started.
     * Environment registration has not occured yet.
     */
    STARTING,

    /**
     * Environment is started and available to execute tasks. This status is used when
     * the environment has just been registered or it was already started.
     */
    STARTED,

    /** The environment has been stopped, thus, no tasks can be processed for associated session. */
    STOPPED,

    /** The environment has failed. Current implementation uses this if resource allocation fails. */
    FAILED
}
