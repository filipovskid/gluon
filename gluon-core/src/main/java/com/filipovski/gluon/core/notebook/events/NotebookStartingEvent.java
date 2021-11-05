package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookId;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class NotebookStartingEvent implements DomainEvent {

    private final NotebookId notebookId;

    private final String sessionId;

    private final Instant occuredOn;

    public NotebookStartingEvent(NotebookId notebookId, String sessionId, Instant occuredOn) {
        if (Strings.isNullOrEmpty(sessionId))
            throw new IllegalArgumentException("sessionId must not be empty");

        this.sessionId = sessionId;
        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static NotebookStartingEvent from(Notebook notebook, String sessionId) {
        return new NotebookStartingEvent(notebook.id(), sessionId, Instant.now());
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
