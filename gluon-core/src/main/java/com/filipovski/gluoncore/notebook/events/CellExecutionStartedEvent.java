package com.filipovski.gluoncore.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluoncore.notebook.NotebookCell;
import com.filipovski.gluoncore.notebook.NotebookCellId;
import com.filipovski.gluoncore.notebook.NotebookId;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

// TODO: Introduce a session within which an execution will happen

@Getter
public class CellExecutionStartedEvent implements DomainEvent {

    private final NotebookCellId cellId;

    private final NotebookId notebookId;

    private final String language;

    private final String code;

    private final Instant startTime;

    private final Instant occuredOn;

    private CellExecutionStartedEvent(NotebookCellId cellId,
                                      NotebookId notebookId,
                                      String language,
                                      String code,
                                      Instant startTime) {
        this.cellId = Objects.requireNonNull(cellId, "cellId must not be null");
        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");

        if (Strings.isNullOrEmpty(language))
            throw new IllegalArgumentException("language must not be empty");

        this.language = language;
        this.code = Strings.isNullOrEmpty(code) ? "" : code;
        this.occuredOn = Instant.now();
    }

    public static CellExecutionStartedEvent from(NotebookCell cell) {
        return new CellExecutionStartedEvent(
                cell.id(),
                cell.getNotebook().id(),
                cell.getLanguage(),
                cell.getCode(),
                Instant.now()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
