package com.vadvincho.kafka.config;

import com.vadvincho.kafka.model.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaServer;

    @Value("${kafka.consumer.groupId}")
    private String kafkaGroupId;

    @Value("${kafka.consumer.clientId}")
    private String kafkaConsumerClientId;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public ConsumerFactory<String, Order> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConsumerClientId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public KafkaConsumer<String, Order> consumerForCountPrice() {
        Map<String, Object> propMap = new HashMap<>();
        propMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        propMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        propMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        propMap.put(ConsumerConfig.GROUP_ID_CONFIG, "thread.group");
        propMap.put(ConsumerConfig.CLIENT_ID_CONFIG, "dbConsumerThread");
        KafkaConsumer<String, Order> consumer = new KafkaConsumer<>(propMap, new StringDeserializer(), getOrderJsonDeserializer());
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    private JsonDeserializer<Order> getOrderJsonDeserializer() {
        JsonDeserializer<Order> orderJsonDeserializer = new JsonDeserializer<>();
        orderJsonDeserializer.addTrustedPackages("com.vadvincho.kafka.model");
        return orderJsonDeserializer;
    }
}
