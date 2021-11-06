package com.filipovski.gluonserver.integration;

import com.filipovski.gluon.common.integration.avro.EnvironmentStartCommandRecord;
import com.filipovski.gluon.common.integration.avro.EnvironmentStopCommandRecord;
import com.filipovski.gluonserver.environment.events.EnvironmentStartCommandEvent;
import com.filipovski.gluonserver.environment.events.EnvironmentStopCommandEvent;
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
@KafkaListener(topics = "#{'${kafka.environment-commands.topic}'}", groupId = "#{'${kafka.environment-commands.group-id}'}")
public class EnvironmentCommandSinkService {

    private final Logger logger = LoggerFactory.getLogger(ExecutionTaskEventSinkService.class);

    private final ApplicationEventPublisher publisher;

    public EnvironmentCommandSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    public void onEnvironmentStartCommand(EnvironmentStartCommandRecord record) {
        EnvironmentStartCommandEvent event = EnvironmentStartCommandEvent.from(
                record.getSessionId(),
                Instant.ofEpochMilli(record.getTimestamp())
        );

        publisher.publishEvent(event);
    }

    @KafkaHandler
    public void onEnvironmentStopCommand(EnvironmentStopCommandRecord record) {
        EnvironmentStopCommandEvent event = EnvironmentStopCommandEvent.from(
                record.getSessionId(),
                Instant.ofEpochMilli(record.getTimestamp())
        );

        publisher.publishEvent(event);
    }
}
