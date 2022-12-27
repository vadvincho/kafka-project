package com.vadvincho.kafka.task;

import com.vadvincho.kafka.model.Order;
import com.vadvincho.kafka.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class SendMessageTask {

    private static final Long PRICE_RANDOM_LIMIT = 5000L;
    private static final Long USER_ID_RANDOM_LIMIT = 3L;

    private final MessageService messageService;

    @Autowired
    public SendMessageTask(MessageService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(fixedRateString = "300")
    public void send() {
        Order order = generateOrder();
        try {
            ListenableFuture<SendResult<String, Order>> listenableFuture = messageService.send(order);
            SendResult<String, Order> result = listenableFuture.get();
            log.debug(String.format("\nProduced:\n object: %s\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d",
                    order,
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().serializedValueSize()));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error sending object {}", order, e);
        }
    }

    private Order generateOrder() {
        BigDecimal price = new BigDecimal(Math.random() * PRICE_RANDOM_LIMIT, new MathContext(6, RoundingMode.HALF_UP));
        return new Order((long) (Math.random() * USER_ID_RANDOM_LIMIT), price.doubleValue(), "Generated message");
    }
}
