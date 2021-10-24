package com.filipovski.gluonserver.integration;

import com.filipovski.gluon.common.integration.avro.TaskStateChangedRecord;
import com.filipovski.gluon.common.integration.avro.TaskStatus;
import com.filipovski.gluonserver.configuration.KafkaConfig;
import com.filipovski.gluonserver.task.events.TaskStateChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TaskStateSourceService {

    private final Logger logger = LoggerFactory.getLogger(TaskStateSourceService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaConfig kafkaConfig;

    public TaskStateSourceService(KafkaTemplate<String, Object> kafkaTemplate, KafkaConfig config) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = config;
    }

    @EventListener
    public void onTaskStateChanged(@NonNull TaskStateChangedEvent event) {
        TaskStateChangedRecord record = TaskStateChangedRecord.newBuilder()
                .setTaskId(event.getTaskId())
                .setStatus(TaskStatus.valueOf(event.getStatus().name()))
                .setResult(event.getResult())
                .setTimestamp(event.occuredOn().toEpochMilli())
                .build();

        kafkaTemplate.send(kafkaConfig.getTaskStatesTopic(), event.getTaskId(), record);
    }

}
