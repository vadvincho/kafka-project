package com.vadvincho.kafka.service;

import com.vadvincho.kafka.model.Order;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface MessageService {

    ListenableFuture<SendResult<String, Order>> send(Order order);

    void consume(Order order, Integer offset, String key, int partition, String topic, long ts);

    void consumeBatch();
}
