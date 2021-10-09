package com.filipovski.gluon.executor.resourcemanager;

import java.util.UUID;

/**
 * {@link WorkerNode} represents information for an instance running an execution
 * environment driver process. This class contains all the necessary information
 * to enable communication with the remote environment.
 *
 * <p>Implementations of this class represent information for worker nodes relevant
 * for the specific external resource manager.</p>
 *
 * TODO: RPC address information might be removed because it becomes available only
 *       after the internal driver process registers itself with the Gluon server.
 *       This means that the resource will be allocated even if the registration fails.
 */

public abstract class WorkerNode {

    private String nodeId;
    private String nodeName;
    private String host;
    private int port;

    /**
     * Worker node containing information essential for the integration with Gluon
     * execution runtime.
     *
     * @param nodeName Name of the node
     * @param host Host address necessary for RPC communication with the driver process.
     * @param port Port on which the RPC server is listening for commands.
     */

    public WorkerNode(String nodeName, String host, int port) {
        this.nodeId = UUID.randomUUID().toString();
        this.nodeName = nodeName;
        this.host = host;
        this.port = port;
    }

}
