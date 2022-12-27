package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface MessageService {

    ListenableFuture<SendResult<String, OrderDto>> send(OrderDto order);

    void consume(OrderDto order, Integer offset, String key, int partition, String topic, long ts);
}
