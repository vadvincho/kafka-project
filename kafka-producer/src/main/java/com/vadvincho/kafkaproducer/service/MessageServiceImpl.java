package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    public MessageServiceImpl(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
