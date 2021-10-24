package com.filipovski.gluon.core.integration;

import com.filipovski.gluon.common.integration.avro.TaskStateChangedRecord;
import com.filipovski.gluon.core.job.events.CellExecutionJobStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@PropertySource("classpath:messaging/messaging.properties")
@KafkaListener(topics = "#{'${kafka.task-states.topic}'}", groupId = "#{'${kafka.task-states.group-id}'}")
public class TaskStateSinkService {

    private final Logger logger = LoggerFactory.getLogger(TaskStateSinkService.class);

    private final  ApplicationEventPublisher publisher;

    public TaskStateSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    public void onTaskStateChanged(TaskStateChangedRecord record) {
        CellExecutionJobStateEvent event = CellExecutionJobStateEvent.from(
                record.getTaskId(),
                record.getStatus().name(),
                record.getResult(),
                Instant.ofEpochMilli(record.getTimestamp())
        );

        publisher.publishEvent(event);
    }
}
