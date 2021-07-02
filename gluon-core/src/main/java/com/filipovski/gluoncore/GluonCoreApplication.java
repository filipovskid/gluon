package com.filipovski.gluoncore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class GluonCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(GluonCoreApplication.class, args);
    }

}
