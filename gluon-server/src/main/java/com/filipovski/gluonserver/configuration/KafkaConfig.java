package com.filipovski.gluonserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:messaging/messaging.properties")
@Configuration
public class KafkaConfig {

    @Value("${kafka.task-states.topic}")
    private String taskStatesTopic;

    public String getTaskStatesTopic() {
        return taskStatesTopic;
    }
}