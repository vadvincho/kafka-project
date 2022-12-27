package com.vadvincho.kafka.handler;

import com.vadvincho.kafka.lock.ReentrantCountLock;
import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.service.Impl.PriceCounterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

@Slf4j
public class ConsumerRecordHandler implements Runnable {

    private final PriceCounterServiceImpl priceCounterService;

    private final ConsumerRecord<String, Order> consumerRecord;

    private final Map<Long, ReentrantCountLock> lockMap;

    public ConsumerRecordHandler(PriceCounterServiceImpl priceCounterServiceImpl,
                                 ConsumerRecord<String, Order> consumerRecord,
                                 Map<Long, ReentrantCountLock> lockMap) {
        this.priceCounterService = priceCounterServiceImpl;
        this.consumerRecord = consumerRecord;
        this.lockMap = lockMap;
    }

    @Override
    public void run() {
        log.info("Process: {}, Offset: {}, By ThreadID: {}", consumerRecord.value(), consumerRecord.offset(),
                Thread.currentThread().getId());
        Order order = consumerRecord.value();
        ReentrantCountLock locker = lockMap.computeIfAbsent(order.getUserId(), v -> new ReentrantCountLock());
        locker.increment();
        log.info("Locker count: {}, order {}", locker.getCount(), order);
        try {
            locker.lock();
            log.debug("Start processing count price with order {}, thread: {}", order, Thread.currentThread().getName());
            priceCounterService.countPrice(order.getUserId(), order.getPrice());
        } finally {
            locker.unlock();
            log.debug("Finished processing count price with order {}, thread: {}", order, Thread.currentThread().getName());
            if (locker.isNotActual()) {
                lockMap.remove(order.getUserId());
            }
        }
    }
}
