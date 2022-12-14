package com.vadvincho.kafka.service;

import com.vadvincho.kafka.model.OrderDto;

public interface MessageService {

    void consume(OrderDto order, Integer offset, String key, int partition, String topic, long ts);
}
