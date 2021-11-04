package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.NotebookCellId;
import com.filipovski.gluon.core.notebook.NotebookCellStatus;
import com.filipovski.gluon.core.notebook.NotebookId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class NotebookCellOutputEvent implements DomainEvent {

    private final NotebookId notebookId;

    private final NotebookCellId cellId;

    private final String data;

    private final Instant occuredOn;

    private NotebookCellOutputEvent(NotebookId notebookId, NotebookCellId cellId, String outputData, Instant occuredOn) {
        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.cellId = Objects.requireNonNull(cellId, "cellId must not be null");
        this.data = Objects.requireNonNull(outputData, "outputData must not be null");
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static NotebookCellOutputEvent from(NotebookId notebookId, NotebookCellId cellId, String outputData, Instant occuredOn) {
        return new NotebookCellOutputEvent(notebookId, cellId, outputData, occuredOn);
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
