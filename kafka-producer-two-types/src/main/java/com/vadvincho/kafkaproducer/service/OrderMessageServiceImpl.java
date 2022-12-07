package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;
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
        log.info("<= Send {}", order);
        try {
            ListenableFuture<SendResult<String, OrderDto>> listenableFuture = orderKafkaTemplate.send(orderTopic, order);
            SendResult<String, OrderDto> result = listenableFuture.get();
            log.info(String.format("\nProduced:\n object: %s\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d",
                    order.toString(),
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().serializedValueSize()));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error when sending object {} to topic {}", order, orderTopic, e);
        }
    }
}
