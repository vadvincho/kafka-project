package com.vadvincho.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:kafka.properties"})
public class KafkaProducerTwoTypesApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerTwoTypesApplication.class, args);
    }
}
