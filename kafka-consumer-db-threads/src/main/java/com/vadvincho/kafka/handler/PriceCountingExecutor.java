package com.vadvincho.kafka.handler;

import com.vadvincho.kafka.lock.ReentrantCountLock;
import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.service.Impl.PriceCounterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PriceCountingExecutor {

    private final Consumer<String, Order> consumer;
    private final PriceCounterServiceImpl priceCounterService;
    private final Map<Long, ReentrantCountLock> lockMap = new ConcurrentHashMap<>();
    private ExecutorService executor;

    @Autowired
    public PriceCountingExecutor(Consumer<String, Order> consumer, PriceCounterServiceImpl priceCounterService) {
        this.consumer = consumer;
        this.priceCounterService = priceCounterService;
    }

    public void execute(int numberOfThreads) {
        executor = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
        ConsumerRecords<String, Order> records = consumer.poll(Duration.ofMillis(100));
        log.info("polled records.counter: {}", records.count());

        getUsersPrices(records);

        for (ConsumerRecord<String, Order> record : records) {
            executor.submit(new ConsumerRecordHandler(priceCounterService, record, lockMap));
        }
    }

    private void getUsersPrices(ConsumerRecords<String, Order> records) {
        List<Order> orders = new ArrayList<>();
        for (ConsumerRecord<String, Order> record : records) {
            Order order = record.value();
            orders.add(order);
        }
        Map<Long, Double> collect = orders.stream()
                .collect(Collectors.groupingBy(Order::getUserId, Collectors.summingDouble(Order::getPrice)));
        log.info("Expected Result {}", collect.toString());
    }

    public void shutdown() {
        if (consumer != null) {
            consumer.close();
        }
        if (executor != null) {
            executor.shutdown();
        }
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                log.warn("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted during shutdown, exiting uncleanly", e);
        }
    }
}
