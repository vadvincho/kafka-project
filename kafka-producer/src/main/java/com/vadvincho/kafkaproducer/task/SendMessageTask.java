package com.vadvincho.kafkaproducer.task;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import com.vadvincho.kafkaproducer.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class SendMessageTask {

    private static final Long RANDOM_LIMIT = 5000L;

    private final MessageService messageService;

    @Autowired
    public SendMessageTask(MessageService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(fixedRateString = "3000")
    public void send() {
        OrderDto order = generateOrder();
        try {
            ListenableFuture<SendResult<String, OrderDto>> listenableFuture = messageService.send(order);
            SendResult<String, OrderDto> result = listenableFuture.get();
            log.info(String.format("\nProduced:\n object: %s\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d",
                    order,
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().serializedValueSize()));
        } catch (InterruptedException|ExecutionException e) {
            log.error("Error sending object {}",order, e);
        }
    }

    private OrderDto generateOrder() {
        return new OrderDto((long) (Math.random() * RANDOM_LIMIT), Math.random() * RANDOM_LIMIT, "Generated message");
    }
}
