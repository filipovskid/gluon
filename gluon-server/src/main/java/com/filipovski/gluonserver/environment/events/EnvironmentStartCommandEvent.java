package com.filipovski.gluonserver.environment.events;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;

import java.time.Instant;

/**
 * Event created at the arrival of a command for starting an environment.
 * Handling this event should ensure, if possible, that an environment will
 * be created, identified by the session id carried by the event.
 */

public class EnvironmentStartCommandEvent implements DomainEvent {

    private String sessionId;

    private Instant timestamp;

    private EnvironmentStartCommandEvent(String sessionId, Instant timestamp) {
        if (Strings.isNullOrEmpty(sessionId))
            throw new IllegalStateException("sessionId must not be empty");

        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }

    public static EnvironmentStartCommandEvent from(String sessionId, Instant timestamp) {
        return new EnvironmentStartCommandEvent(sessionId, timestamp);
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }
}
