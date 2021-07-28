package com.filipovski.gluoncore.job.events;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class CellExecutionJobCreatedEvent implements DomainEvent {

    private final String jobId;

    private final String sessionid;

    private final String language;

    private final String code;

    private final Instant startTime;

    private final Instant occuredOn;

    public CellExecutionJobCreatedEvent(String id,
                                        String sessionId,
                                        String language,
                                        String code,
                                        Instant startTime) {
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");

        if (Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("id must not be empty");

        if (Strings.isNullOrEmpty(sessionId))
            throw new IllegalArgumentException("sessionId must not be empty");

        if (Strings.isNullOrEmpty(language))
            throw new IllegalArgumentException("language must not be empty");

        if (Strings.isNullOrEmpty(code))
            throw new IllegalArgumentException("code must not be empty");

        this.jobId = id;
        this.sessionid = sessionId;
        this.language = language;
        this.code = code;
        this.occuredOn = Instant.now();
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
