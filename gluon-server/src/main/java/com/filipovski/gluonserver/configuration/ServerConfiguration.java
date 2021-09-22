package com.filipovski.gluonserver.configuration;

import com.filipovski.gluon.docker.DockerResourceManagerBackend;
import com.filipovski.gluon.docker.GluonDockerClientFactory;
import com.filipovski.gluon.executor.resourcemanager.ResourceManagerBackend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

    @Bean
    public ResourceManagerBackend resourceManagerBackend() {
        return new DockerResourceManagerBackend(
                GluonDockerClientFactory.createDockerClient()
        );
    }

}
