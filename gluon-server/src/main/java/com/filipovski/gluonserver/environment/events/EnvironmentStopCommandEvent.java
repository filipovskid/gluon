package com.filipovski.gluonserver.environment.events;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;

import java.time.Instant;

/**
 * Event created at the client service command to stop an environment.
 * Handling this event should result in stopping the environment identified
 * by the session this event carries.
 */

public class EnvironmentStopCommandEvent implements DomainEvent {

    private final String sessionId;

    private final Instant timestamp;

    private EnvironmentStopCommandEvent(String sessionId, Instant timestamp) {
        if (Strings.isNullOrEmpty(sessionId))
            throw new IllegalStateException("sessionId must not be empty");

        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }

    public static EnvironmentStopCommandEvent from(String sessionId, Instant timestamp) {
        return new EnvironmentStopCommandEvent(sessionId, timestamp);
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Instant occuredOn() {
        return timestamp;
    }
}
