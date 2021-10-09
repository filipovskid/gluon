package com.filipovski.gluonserver.integration;

import com.filipovski.gluon.common.integration.avro.ExecutionTaskCreatedRecord;
import com.filipovski.gluonserver.task.events.ExecutionStatementSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:messaging/messaging.properties")
@KafkaListener(topics = "#{'${kafka.execution.tasks.topic}'}", groupId = "#{'${kafka.execution.tasks.group-id}'}")
public class ExecutionTaskEventSinkService {
    
    private final Logger logger = LoggerFactory.getLogger(ExecutionTaskEventSinkService.class);
    private final ApplicationEventPublisher eventPublisher;

    public ExecutionTaskEventSinkService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @KafkaHandler
    public void onExecutionTaskCreatedEvent(ExecutionTaskCreatedRecord record) {
        ExecutionStatementSubmittedEvent event = new ExecutionStatementSubmittedEvent(
                record.getId(),
                record.getLanguage(),
                record.getCode()
        );

        this.eventPublisher.publishEvent(event);
    }

}
