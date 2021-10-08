package com.filipovski.gluonserver.configuration;

import com.filipovski.gluon.docker.DockerResourceManagerFactory;
import com.filipovski.gluon.executor.resourcemanager.ResourceManagerBackend;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigResolveOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ServerConfiguration {

    private Config gluonConfig;

    @Bean
    @DependsOn({"gluon-config"})
    public ResourceManagerBackend resourceManagerBackend() {
        return DockerResourceManagerFactory.createResourceManagerBackend(this.gluonConfig);
    }

    @Bean("gluon-config")
    public Config gluonServerConfig() {
        this.gluonConfig = ConfigFactory
                .load(ConfigParseOptions.defaults().setAllowMissing(false),
                        ConfigResolveOptions.noSystem()).resolve();

        return this.gluonConfig;
    }
}
