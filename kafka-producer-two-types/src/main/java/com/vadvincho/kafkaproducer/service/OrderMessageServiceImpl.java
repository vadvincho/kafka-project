package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderMessageServiceImpl implements MessageService<OrderDto> {

    @Value("${kafka.order.topic}")
    private String orderTopic;

    private final KafkaTemplate<String, OrderDto> orderKafkaTemplate;

    @Autowired
    public OrderMessageServiceImpl(KafkaTemplate<String, OrderDto> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    @Override
    public void send(OrderDto order) {
        log.info("<= Send {} by {}", order, this.getClass().getName());
        orderKafkaTemplate.send(orderTopic, order);
    }
}
