package com.vadvincho.kafkaproducer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vadvincho.kafkaproducer.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public MessageServiceImpl(KafkaTemplate<String, OrderDto> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(OrderDto order) {
        log.info("<= Send {}", order.toString());
        kafkaTemplate.send("order", order.getUserId().toString(), order);
    }

    @Override
    @KafkaListener(topics = {"order"}, containerFactory = "listenerContainerFactory")
    public void consume(OrderDto order) {
        log.info("=> consumed {}", order.toString());
    }
}
