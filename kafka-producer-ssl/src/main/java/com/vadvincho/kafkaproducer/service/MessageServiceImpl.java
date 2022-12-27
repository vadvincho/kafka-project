package com.vadvincho.kafkaproducer.service;

import com.vadvincho.kafkaproducer.dto.OrderDto;
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

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    public MessageServiceImpl(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ListenableFuture<SendResult<String, OrderDto>> send(OrderDto order) {
        log.info("<= Send {}", order.toString());
        return kafkaTemplate.send("order", order.getUserId().toString(), order);
    }

    @Override
    @KafkaListener(topics = {"order"}, containerFactory = "listenerContainerFactory")
    public void consume(final @Payload OrderDto order,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
        log.info(String.format("\n#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, order, offset, key, partition, topic));
    }
}
