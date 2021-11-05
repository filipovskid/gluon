package com.filipovski.gluon.core.notebook;

public enum NotebookStatus {
    /** Notebook is starting. */
    STARTING,

    /** Notebook is started and available for execution. */
    STARTED,

    /** Notebook is in the process of being stopped. */
    STOPPING,

    /** The notebook is stopped or newly created. */
    STOPPED,

    /** Notebook failed to start */
    FAILED
}
