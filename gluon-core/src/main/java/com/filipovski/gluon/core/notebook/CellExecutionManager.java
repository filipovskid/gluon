package com.filipovski.gluon.core.notebook;

import com.filipovski.gluon.core.job.CellExecutionJob;
import com.filipovski.gluon.core.job.Job;
import com.filipovski.gluon.core.job.JobRepository;
import com.filipovski.gluon.core.job.events.CellExecutionJobCreatedEvent;
import com.filipovski.gluon.core.job.events.CellExecutionJobOutputEvent;
import com.filipovski.gluon.core.job.events.CellExecutionJobStateEvent;
import com.filipovski.gluon.core.notebook.events.CellExecutionStartedEvent;
import com.filipovski.gluon.core.notebook.events.NotebookCellOutputEvent;
import com.filipovski.gluon.core.notebook.events.NotebookCellStateUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

// TODO: Implement a service that can generate execution session IDs
// TODO: Each cell status or cell output integration event generates two local events.
//       There might be no need for such complexity - utilize notebook service instead
//       of pushing events.

@Service
public class CellExecutionManager {

    private final Logger logger = LoggerFactory.getLogger(CellExecutionManager.class);
    private final ApplicationEventPublisher eventPublisher;
    private final JobRepository jobRepository;

    public CellExecutionManager(ApplicationEventPublisher eventPublisher, JobRepository jobRepository) {
        this.eventPublisher = eventPublisher;
        this.jobRepository = jobRepository;
    }

    @EventListener
    public void onCellExecutionStarted(@NonNull CellExecutionStartedEvent event) {
        // TODO: Create a job that knows about the entity being executed.

        Job job = new CellExecutionJob(
                event.getSessionId(),
                event.getNotebookId(),
                event.getCellId()
        );
        job = this.jobRepository.save(job);
        logger.info("Cell execution job created [{}]", job.getId());

        CellExecutionJobCreatedEvent jobCreatedEvent = new CellExecutionJobCreatedEvent(
                job.getId(),
                job.getSessionId(),
                event.getLanguage(),
                event.getCode(),
                event.getStartTime(),
                event.getOccuredOn()
        );
        eventPublisher.publishEvent(jobCreatedEvent);
    }

    @EventListener
    public void onCellExecutionStateChange(@NonNull CellExecutionJobStateEvent event) {
        CellExecutionJob job = this.jobRepository.findById(event.getJobId())
                .map(j -> (CellExecutionJob) j)
                .orElseThrow(() -> handleJobNotFound(event.getJobId()));
        NotebookCellStateUpdateEvent stateUpdateEvent = NotebookCellStateUpdateEvent.from(
                job.getNotebookId(),
                job.getCellId(),
                event.getStatus(),
                event.occuredOn()
        );

        eventPublisher.publishEvent(stateUpdateEvent);
    }

    @EventListener
    public void onCellExecutionOutputArrival(@NonNull CellExecutionJobOutputEvent event) {
        CellExecutionJob job = this.jobRepository.findById(event.getJobId())
                .map(j -> (CellExecutionJob) j)
                .orElseThrow(() -> handleJobNotFound(event.getJobId()));

        NotebookCellOutputEvent outputEvent = NotebookCellOutputEvent.from(
                job.getNotebookId(),
                job.getCellId(),
                event.getData(),
                event.getOccuredOn()
        );
        eventPublisher.publishEvent(outputEvent);
    }

    private RuntimeException handleJobNotFound(String jobId) {
        logger.warn("Cell execution job [{}] has not been found!", jobId);
        return new RuntimeException(String.format("Cell execution job %s not found!", jobId));
    }
}
