package com.filipovski.gluon.core.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@PropertySource("classpath:messaging/messaging.properties")
@Configuration
public class KafkaConfig {

    @Value("${kafka.execution.tasks.topic}")
    private String tasksTopic;

    @Value("${kafka.environment-commands.topic}")
    private String environmentCommandsTopic;
}
