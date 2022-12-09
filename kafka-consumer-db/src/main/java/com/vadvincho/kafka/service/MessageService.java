package com.vadvincho.kafka.service;

import com.vadvincho.kafka.model.Order;

public interface MessageService {

    void consume(Order order, Integer offset, String key, int partition, String topic, long ts);
}
