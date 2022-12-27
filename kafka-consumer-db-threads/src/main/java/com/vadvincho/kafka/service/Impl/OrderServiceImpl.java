package com.vadvincho.kafka.service.Impl;

import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.repository.OrderRepository;
import com.vadvincho.kafka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }
}
