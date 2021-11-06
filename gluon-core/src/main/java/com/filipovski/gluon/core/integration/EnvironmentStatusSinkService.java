package com.filipovski.gluon.core.integration;

import com.filipovski.gluon.common.integration.avro.EnvironmentStatusChangedRecord;
import com.filipovski.gluon.core.notebook.NotebookStatus;
import com.filipovski.gluon.core.notebook.events.EnvironmentStatusChangedEvent;
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
@KafkaListener(topics = "#{'${kafka.environment-status.topic}'}", groupId = "#{'${kafka.environment-status.group-id}'}")
public class EnvironmentStatusSinkService {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentStatusSinkService.class);

    private final ApplicationEventPublisher publisher;

    public EnvironmentStatusSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    public void onEnvironmentStatusChange(EnvironmentStatusChangedRecord record) {
        EnvironmentStatusChangedEvent event = EnvironmentStatusChangedEvent.from(
                record.getSessionId(),
                NotebookStatus.valueOf(record.getStatus().name()),
                record.getMessage(),
                Instant.ofEpochMilli(record.getTimestamp())
        );

        publisher.publishEvent(event);
    }
}
