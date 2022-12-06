package com.vadvincho.kafka.service;

import com.vadvincho.kafka.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Override
    @KafkaListener(topics = {"order"}, containerFactory = "listenerContainerFactory")
    public void consume(OrderDto order) {
        log.info("=> consumed {}", order.toString());
    }
}
