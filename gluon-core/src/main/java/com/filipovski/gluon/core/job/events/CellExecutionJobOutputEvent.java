package com.filipovski.gluon.core.job.events;

import com.filipovski.common.domain.DomainEvent;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class CellExecutionJobOutputEvent implements DomainEvent {

    private final String jobId;

    private final String data;

    private final Instant occuredOn;

    private CellExecutionJobOutputEvent(String jobId, String outputData, Instant occuredOn) {
        if (Strings.isNullOrEmpty(jobId))
            throw new IllegalArgumentException("jobId must not be empty");

        this.jobId = jobId;
        this.data = Objects.requireNonNull(outputData, "outputData must not be null!");
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static CellExecutionJobOutputEvent from(String jobId, String outputData, Instant occuredOn) {
        return new CellExecutionJobOutputEvent(jobId, outputData, occuredOn);
    }

    @Override
    public Instant occuredOn() {
        return null;
    }
}
