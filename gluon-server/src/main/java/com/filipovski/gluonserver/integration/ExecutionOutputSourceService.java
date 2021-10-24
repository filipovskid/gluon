package com.filipovski.gluonserver.integration;

import com.filipovski.gluon.common.integration.avro.ExecutionOutputRecord;
import com.filipovski.gluonserver.configuration.KafkaConfig;
import com.filipovski.gluonserver.task.events.ExecutionOutputArrivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ExecutionOutputSourceService {

    private final Logger logger = LoggerFactory.getLogger(ExecutionOutputSourceService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaConfig kafkaConfig;

    public ExecutionOutputSourceService(KafkaTemplate<String, Object> kafkaTemplate, KafkaConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = config;
    }

    @EventListener
    public void onExecutionOutputArrival(@NonNull ExecutionOutputArrivedEvent event) {
        ExecutionOutputRecord record = ExecutionOutputRecord.newBuilder()
                .setTaskId(event.getTaskId())
                .setData(event.getData())
                .setTimestamp(event.occuredOn().toEpochMilli())
                .build();

        kafkaTemplate.send(kafkaConfig.getExecutionOutputTopic(), event.getTaskId(), record);
    }
}
