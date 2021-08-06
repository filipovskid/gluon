package com.filipovski.gluon.docker;

import com.filipovski.gluon.executor.resourcemanager.WorkerNode;

public class DockerWorkerNode extends WorkerNode {
    /**
     * Worker node containing information essential for the integration with Gluon
     * execution runtime.
     *
     * @param nodeName Name of the node
     * @param host     Host address necessary for RPC communication with the driver process.
     * @param port     Port on which the RPC server is listening for commands.
     */
    public DockerWorkerNode(String nodeName, String host, int port) {
        super(nodeName, host, port);
    }
}
