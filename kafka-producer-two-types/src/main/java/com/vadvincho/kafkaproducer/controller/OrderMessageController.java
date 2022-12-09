package com.vadvincho.kafkaproducer.controller;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import com.vadvincho.kafkaproducer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/message")
public class OrderMessageController {

    private final MessageService<OrderDto> messageService;

    @Autowired
    public OrderMessageController(MessageService<OrderDto> messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public void send(@RequestBody OrderDto order) {
        messageService.send(order);
    }
}
