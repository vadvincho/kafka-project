package com.vadvincho.kafka.service;

import com.vadvincho.kafka.model.Order;

public interface OrderService {

    void save(Order order);
}
