package com.vadvincho.kafkaproducer.controller;

import com.vadvincho.kafkaproducer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/string/message")
public class StringMessageController {

    private final MessageService<String> messageService;

    @Autowired
    public StringMessageController(MessageService<String> messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send/{message}")
    public void send(@PathVariable String message) {
        messageService.send(message);
    }
}
