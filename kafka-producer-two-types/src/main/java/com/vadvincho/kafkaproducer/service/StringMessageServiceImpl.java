package com.vadvincho.kafkaproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

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
        log.info("<= Send {}", message);
        try {
            ListenableFuture<SendResult<String, String>> listenableFuture = stringKafkaTemplate.send(stringTopic, message);
            SendResult<String, String> result = listenableFuture.get();
            log.info(String.format("\nProduced:\n object: %s\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d",
                    message,
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().serializedValueSize()));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error when sending object {} to topic {}", message, stringTopic, e);
        }
    }
}
