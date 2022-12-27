package com.vadvincho.kafka.service;

import java.util.Map;

public interface PriceCounterService {

    void countPrice(Long userId, Double price);

    Map<Long, Double> getCountPriceMap();

    void clearPriceMap();
}
