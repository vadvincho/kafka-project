package com.vadvincho.kafka.service.Impl;

import com.vadvincho.kafka.handler.PriceCountingExecutor;
import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.service.MessageService;
import com.vadvincho.kafka.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    private final OrderService orderService;

    private final PriceCountingExecutor priceCountingExecutor;

    @Autowired
    public MessageServiceImpl(KafkaTemplate<String, Order> kafkaTemplate, OrderService orderService, PriceCountingExecutor priceCountingExecutor) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderService = orderService;
        this.priceCountingExecutor = priceCountingExecutor;
    }

    @Override
    public ListenableFuture<SendResult<String, Order>> send(Order order) {
        log.info("<= Send {}", order.toString());
        return kafkaTemplate.send("order", order.getUserId().toString(), order);
    }

    @Override
    @KafkaListener(topics = {"order"}, containerFactory = "listenerContainerFactory")
    public void consume(final @Payload Order order,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
        log.debug(String.format("\n#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, order, offset, key, partition, topic));
        orderService.save(order);
    }

    @Override
    public void consumeBatch() {
        priceCountingExecutor.execute(10);
    }
}
