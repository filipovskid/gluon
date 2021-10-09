package com.filipovski.gluon.core.integration;

import com.filipovski.gluon.common.integration.avro.ExecutionTaskCreatedRecord;
import com.filipovski.gluon.core.config.KafkaConfig;
import com.filipovski.gluon.core.job.events.CellExecutionJobCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ExecutionEventSourceService {

    private final Logger logger = LoggerFactory.getLogger(ExecutionEventSourceService.class);
    private final KafkaConfig config;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ExecutionEventSourceService(KafkaTemplate<String, Object> kafkaTemplate,
                                       KafkaConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.config = config;
    }

    @EventListener
    public void onCellExecutionJobCreated(@NonNull CellExecutionJobCreatedEvent event) {
        ExecutionTaskCreatedRecord remoteEvent = ExecutionTaskCreatedRecord.newBuilder()
                .setId(event.getJobId())
                .setLanguage(event.getLanguage())
                .setCode(event.getCode())
                .build();

        kafkaTemplate.send(config.getTasksTopic(), event.getSessionid(), remoteEvent);
    }

}
