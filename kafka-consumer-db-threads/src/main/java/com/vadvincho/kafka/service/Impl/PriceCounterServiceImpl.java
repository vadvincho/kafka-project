package com.vadvincho.kafka.service.Impl;

import com.vadvincho.kafka.service.PriceCounterService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PriceCounterServiceImpl implements PriceCounterService {

    private final Map<Long, Double> countPriceMap = new HashMap<>();

    @Override
    public void countPrice(Long userId, Double price) {
        countPriceMap.compute(userId, (k, v) -> (v == null) ? price : v + price);
    }

    @Override
    public Map<Long, Double> getCountPriceMap() {
        return countPriceMap;
    }

    @Override
    public void clearPriceMap() {
        countPriceMap.clear();
    }
}
