package com.vadvincho.kafka.controller;

import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.service.MessageService;
import com.vadvincho.kafka.service.Impl.PriceCounterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    private final PriceCounterServiceImpl priceCounterService;

    @Autowired
    public MessageController(MessageService messageService, PriceCounterServiceImpl priceCounterService) {
        this.messageService = messageService;
        this.priceCounterService = priceCounterService;
    }

    @PostMapping("/send")
    public void send(@RequestBody Order order) {
        messageService.send(order);
    }

    @GetMapping("/consume")
    public void consume() {
        priceCounterService.clearPriceMap();
        messageService.consumeBatch();
    }

    @GetMapping("/price")
    public Map<Long, Double> getPrice() {
        return priceCounterService.getCountPriceMap();
    }
}
