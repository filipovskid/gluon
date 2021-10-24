package com.filipovski.gluon.core.notebook;

/**
 * States in which {@link NotebookCell} can be after its creation.
 */

public enum NotebookCellStatus {
    /** The cell is newly created and has not been executed jet. */
    CREATED,

    /** Cell is being dispatched for execution, but has not started running yet. */
    PENDING,

    /** The cell is being executed. */
    RUNNING,

    /** Cell execution is being cancelled. */
    CANCELLING,

    /** The cell has been successfully executed. */
    COMPLETED,

    /** Cell execution has been canceled. */
    CANCELED,

    /** The cell execution has failed. */
    FAILED
}
