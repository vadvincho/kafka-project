package com.vadvincho.kafkaproducer.config;

import com.vadvincho.kafkaproducer.dto.OrderDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

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
    private String kafkaConsumerId;

    @Bean
    public ConsumerFactory<String, OrderDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConsumerId);
//        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 5242880);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }
}
