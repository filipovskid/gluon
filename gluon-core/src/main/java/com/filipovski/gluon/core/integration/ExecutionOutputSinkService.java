package com.filipovski.gluon.core.integration;

import com.filipovski.gluon.common.integration.avro.ExecutionOutputRecord;
import com.filipovski.gluon.core.job.events.CellExecutionJobOutputEvent;
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
@KafkaListener(topics = "#{'${kafka.execution-outputs.topic}'}", groupId = "#{'${kafka.execution-outputs.group-id}'}")
public class ExecutionOutputSinkService {

    private final Logger logger = LoggerFactory.getLogger(TaskStateSinkService.class);

    private final ApplicationEventPublisher publisher;

    public ExecutionOutputSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    public void onExecutionOutputArrival(ExecutionOutputRecord record) {
        CellExecutionJobOutputEvent event = CellExecutionJobOutputEvent.from(
                record.getTaskId(),
                record.getData(),
                Instant.ofEpochMilli(record.getTimestamp())
        );

        publisher.publishEvent(event);
    }
}
