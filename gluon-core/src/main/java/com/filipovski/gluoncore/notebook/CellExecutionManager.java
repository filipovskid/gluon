package com.filipovski.gluoncore.notebook;

import com.filipovski.gluoncore.job.CellExecutionJob;
import com.filipovski.gluoncore.job.Job;
import com.filipovski.gluoncore.job.JobRepository;
import com.filipovski.gluoncore.job.events.CellExecutionJobCreatedEvent;
import com.filipovski.gluoncore.notebook.events.CellExecutionStartedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

// TODO: Implement a service that can generate execution session IDs

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
    public void onCellExecutionStarted(@NotNull CellExecutionStartedEvent event) {
        // TODO: Create a job that knows about the entity being executed.

        Job job = new CellExecutionJob(
                "test-session-id",
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
                event.getStartTime()
        );
        eventPublisher.publishEvent(jobCreatedEvent);
    }


}
