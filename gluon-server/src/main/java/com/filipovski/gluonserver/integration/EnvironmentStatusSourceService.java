package com.filipovski.gluonserver.integration;

import com.filipovski.gluon.common.integration.avro.EnvironmentStatus;
import com.filipovski.gluon.common.integration.avro.EnvironmentStatusChangedRecord;
import com.filipovski.gluonserver.configuration.KafkaConfig;
import com.filipovski.gluonserver.environment.events.EnvironmentStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentStatusSourceService {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentStatusSourceService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaConfig kafkaConfig;

    public EnvironmentStatusSourceService(KafkaTemplate<String, Object> kafkaTemplate,
                                          KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfig = kafkaConfig;
    }

    @EventListener
    public void onEnvironmentStatusChange(EnvironmentStatusChangedEvent event) {
        EnvironmentStatusChangedRecord record = EnvironmentStatusChangedRecord.newBuilder()
                .setSessionId(event.getSessionId())
                .setStatus(EnvironmentStatus.valueOf(event.getStatus().name()))
                .setMessage(event.getMessage())
                .setTimestamp(event.getTimestamp().toEpochMilli())
                .build();

        kafkaTemplate.send(kafkaConfig.getEnvironmentStatusTopic(), record.getSessionId(), record);
    }
}
