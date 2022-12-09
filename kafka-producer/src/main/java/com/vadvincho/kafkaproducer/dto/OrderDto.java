package com.vadvincho.kafkaproducer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {

    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime created = LocalDateTime.now();

    private Double price;

    private String message;

    public OrderDto() {
    }

    public OrderDto(Long userId, Double price, String message) {
        this.userId = userId;
        this.price = price;
        this.message = message;
    }
}
