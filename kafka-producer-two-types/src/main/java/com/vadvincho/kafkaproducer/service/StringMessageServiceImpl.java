package com.vadvincho.kafkaproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StringMessageServiceImpl implements MessageService<String> {

    @Value("${kafka.string.topic}")
    private String stringTopic;

    private final KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    public StringMessageServiceImpl(KafkaTemplate<String, String> stringKafkaTemplate) {
        this.stringKafkaTemplate = stringKafkaTemplate;
    }

    @Override
    public void send(String message) {
        log.info("<= Send {} by {}", message, this.getClass().getName());
        stringKafkaTemplate.send(stringTopic, message);
    }
}
