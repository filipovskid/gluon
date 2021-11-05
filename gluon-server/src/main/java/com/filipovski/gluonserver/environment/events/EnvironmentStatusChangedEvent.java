package com.filipovski.gluonserver.environment.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluonserver.environment.EnvironmentStatus;
import com.google.common.base.Strings;

import java.time.Instant;
import java.util.Objects;

/**
 * Event emitted as a result of changes in the environment status. These changes
 * happen during environment resource allocation, registration and management (eg. stopping).
 *
 * <p>Further improvements should use this event to dispatch environment status
 * changes due to failures.</p>
 */

public class EnvironmentStatusChangedEvent implements DomainEvent {

    private String sessionId;

    private EnvironmentStatus status;

    private String message;

    private Instant timestamp;

    private EnvironmentStatusChangedEvent(String sessionId,
                                          EnvironmentStatus status,
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
                                                    EnvironmentStatus status,
                                                    String message,
                                                    Instant timestamp) {
        return new EnvironmentStatusChangedEvent(sessionId, status, message, timestamp);
    }

    public String getSessionId() {
        return sessionId;
    }

    public EnvironmentStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }

    private void assertStringNotEmpty(String o, String name) {
        if (Strings.isNullOrEmpty(o))
            throw new IllegalStateException(name + " must not be empty");
    }
}
