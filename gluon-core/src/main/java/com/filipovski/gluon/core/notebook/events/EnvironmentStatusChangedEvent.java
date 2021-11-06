package com.filipovski.gluon.core.notebook.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.NotebookStatus;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class EnvironmentStatusChangedEvent implements DomainEvent {

    private final String sessionId;

    private final NotebookStatus status;

    private final String message;

    private final Instant timestamp;

    private EnvironmentStatusChangedEvent(String sessionId,
                                          NotebookStatus status,
                                          String message,
                                          Instant timestamp) {
        assertStringNotEmpty(sessionId, "sessionId");
        assertStringNotEmpty(message, "message");

        this.sessionId = sessionId;
        this.status = Objects.requireNonNull(status, "status must not be null!");
        this.message = message;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null!");
    }

    public static EnvironmentStatusChangedEvent from(String sessionId,
                                                     NotebookStatus status,
                                                     String message,
                                                     Instant timestamp) {
        return new EnvironmentStatusChangedEvent(sessionId, status, message, timestamp);
    }

    private void assertStringNotEmpty(String o, String name) {
        if (Strings.isNullOrEmpty(o))
            throw new IllegalStateException(name + " must not be empty");
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }
}
