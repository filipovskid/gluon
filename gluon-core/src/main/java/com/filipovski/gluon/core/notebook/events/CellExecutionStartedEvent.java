package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.NotebookId;
import com.filipovski.gluon.core.notebook.NotebookCell;
import com.filipovski.gluon.core.notebook.NotebookCellId;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

// TODO: Introduce a session within which an execution will happen

@Getter
public class CellExecutionStartedEvent implements DomainEvent {

    private final NotebookCellId cellId;

    private final NotebookId notebookId;

    private final String sessionId;

    private final String language;

    private final String code;

    private final Instant startTime;

    private final Instant occuredOn;

    private CellExecutionStartedEvent(NotebookCellId cellId,
                                      NotebookId notebookId,
                                      String sessionId,
                                      String language,
                                      String code,
                                      Instant startTime) {
        this.cellId = Objects.requireNonNull(cellId, "cellId must not be null");
        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");

        assertStringNotEmpty(sessionId, "sessionId");
        assertStringNotEmpty(language, "language");

        this.sessionId = sessionId;
        this.language = language;
        this.code = Strings.isNullOrEmpty(code) ? "" : code;
        this.occuredOn = Instant.now();
    }

    public static CellExecutionStartedEvent from(NotebookCell cell, String sessionId) {
        return new CellExecutionStartedEvent(
                cell.id(),
                cell.getNotebook().id(),
                sessionId,
                cell.getLanguage(),
                cell.getCode(),
                Instant.now()
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }

    private void assertStringNotEmpty(String o, String name) {
        if (Strings.isNullOrEmpty(o))
            throw new IllegalArgumentException(name + " must not be empty");
    }
}
