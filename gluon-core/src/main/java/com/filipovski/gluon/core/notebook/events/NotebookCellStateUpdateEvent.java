package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.NotebookCellId;
import com.filipovski.gluon.core.notebook.NotebookCellStatus;
import com.filipovski.gluon.core.notebook.NotebookId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class NotebookCellStateUpdateEvent implements DomainEvent {

    private final NotebookId notebookId;

    private final NotebookCellId cellId;

    private final NotebookCellStatus status;

    private final Instant occuredOn;

    private NotebookCellStateUpdateEvent(NotebookId notebookId, NotebookCellId cellId, NotebookCellStatus status, Instant occuredOn) {
        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.cellId = Objects.requireNonNull(cellId, "cellId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static NotebookCellStateUpdateEvent from(NotebookId notebookId,
                                                    NotebookCellId cellId,
                                                    NotebookCellStatus status,
                                                    Instant occuredOn) {
        return new NotebookCellStateUpdateEvent(notebookId, cellId, status, occuredOn);
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
