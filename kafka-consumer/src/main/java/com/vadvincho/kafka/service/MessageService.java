package com.vadvincho.kafka.service;

import com.vadvincho.kafka.dto.OrderDto;

public interface MessageService {

    void consume(OrderDto order);
}