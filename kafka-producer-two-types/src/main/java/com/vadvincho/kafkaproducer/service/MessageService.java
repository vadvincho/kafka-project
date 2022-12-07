package com.vadvincho.kafkaproducer.service;

public interface MessageService<T> {

    void send(T message);
}
