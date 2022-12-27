package com.vadvincho.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaProducerSSLApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerSSLApplication.class, args);
    }
}
