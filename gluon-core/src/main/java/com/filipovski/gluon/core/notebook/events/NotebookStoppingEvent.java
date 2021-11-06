package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.Notebook;
import com.filipovski.gluon.core.notebook.NotebookId;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class NotebookStoppingEvent implements DomainEvent {

    private final NotebookId notebookId;

    private final String sessionId;

    private final Instant timestamp;

    private NotebookStoppingEvent(NotebookId notebookId, String sessionId, Instant timestamp) {
        if (Strings.isNullOrEmpty(sessionId))
            throw new IllegalArgumentException("sessionId must not be empty");

        this.notebookId = Objects.requireNonNull(notebookId, "notebookId must not be null");
        this.sessionId = sessionId;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
    }

    public static NotebookStoppingEvent from(Notebook notebook, String sessionId) {
        return new NotebookStoppingEvent(notebook.id(), sessionId, Instant.now());
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }
}
