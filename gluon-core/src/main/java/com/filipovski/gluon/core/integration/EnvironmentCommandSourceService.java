package com.filipovski.gluon.core.integration;

import com.filipovski.gluon.common.integration.avro.EnvironmentStartCommandRecord;
import com.filipovski.gluon.common.integration.avro.EnvironmentStopCommandRecord;
import com.filipovski.gluon.core.config.KafkaConfig;
import com.filipovski.gluon.core.notebook.events.NotebookStartingEvent;
import com.filipovski.gluon.core.notebook.events.NotebookStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class EnvironmentCommandSourceService {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentCommandSourceService.class);
    private final KafkaConfig config;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EnvironmentCommandSourceService(KafkaTemplate<String, Object> kafkaTemplate,
                                           KafkaConfig config) {
        this.config = config;
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotebookStartup(@NonNull NotebookStartingEvent event) {
        EnvironmentStartCommandRecord record = EnvironmentStartCommandRecord.newBuilder()
                .setSessionId(event.getSessionId())
                .setTimestamp(event.occuredOn().toEpochMilli())
                .build();

        kafkaTemplate.send(config.getEnvironmentCommandsTopic(), record.getSessionId(), record);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotebookStopping(@NonNull NotebookStoppingEvent event) {
        EnvironmentStopCommandRecord record = EnvironmentStopCommandRecord.newBuilder()
                .setSessionId(event.getSessionId())
                .setTimestamp(event.getTimestamp().toEpochMilli())
                .build();

        kafkaTemplate.send(config.getEnvironmentCommandsTopic(), record.getSessionId(), record);
    }

}
