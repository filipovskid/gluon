package com.filipovski.gluon.core.job.events;

import com.filipovski.common.domain.DomainEvent;
import com.filipovski.gluon.core.notebook.NotebookCellStatus;
import com.google.common.base.Strings;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class CellExecutionJobStateEvent implements DomainEvent {

    private final String jobId;

    private final NotebookCellStatus status;

    private final String result;

    private final Instant occuredOn;

    private CellExecutionJobStateEvent(String jobId, NotebookCellStatus status, String result, Instant occuredOn) {
        if (Strings.isNullOrEmpty(jobId))
            throw new IllegalArgumentException("jobId must not be empty");

        this.jobId = jobId;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.result = result;
        this.occuredOn = Objects.requireNonNull(occuredOn, "occuredOn must not be null");
    }

    public static CellExecutionJobStateEvent from(String jobId, String status, String result, Instant occuredOn) {
        return new CellExecutionJobStateEvent(
                jobId,
                NotebookCellStatus.valueOf(status),
                result,
                occuredOn
        );
    }

    @Override
    public Instant occuredOn() {
        return occuredOn;
    }
}
