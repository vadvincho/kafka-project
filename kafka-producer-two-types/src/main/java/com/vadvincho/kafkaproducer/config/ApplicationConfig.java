package com.vadvincho.kafkaproducer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:kafka.properties"})
public class ApplicationConfig {
}
