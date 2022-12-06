package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;

public interface MessageService {

    void send(OrderDto order);

    void consume(OrderDto order);
}
