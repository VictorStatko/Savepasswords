package com.statkovit.personalAccountsService.configuration;

import com.statkolibraries.kafkaUtils.CustomKafkaHeaders;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkovit.personalAccountsService.kafka.RedisKafkaManager;
import com.statkovit.personalAccountsService.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.requests.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.backoff.FixedBackOff;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;
    private final CustomProperties customProperties;
    private final RedisKafkaManager redisKafkaManager;

    public static final String LISTENER_FACTORY_NAME = "kafkaListenerContainerFactory";

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(
                ProducerConfig.TRANSACTIONAL_ID_CONFIG,
                customProperties.getKafka().getProducer().getTransactionIdPrefix()
        );
        return props;
    }

    @Bean
    public ProducerFactory<String, KafkaMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager transactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return props;
    }


    @Bean
    public ConsumerFactory<String, KafkaMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer2<>(new JsonDeserializer<>(KafkaMessage.class)));
    }

    @Bean(name = LISTENER_FACTORY_NAME)
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> kafkaListenerContainerFactory() {
        DeadLetterPublishingRecoverer recoverer = new CustomDeadLetterPublishingRecoverer(
                kafkaTemplate(),
                (r, e) -> new TopicPartition(r.topic() + ".failures", r.partition())
        );

        ErrorHandler errorHandler = new SeekToCurrentErrorHandler(
                recoverer, new FixedBackOff(FixedBackOff.DEFAULT_INTERVAL, 10L)
        );

        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckOnError(false);
        factory.setAckDiscarded(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setTransactionManager(transactionManager());
        factory.setRecordFilterStrategy(recordFilterStrategy());
        factory.setErrorHandler(errorHandler);

        return factory;
    }

    private RecordFilterStrategy<String, KafkaMessage> recordFilterStrategy() {
        return consumerRecord -> {
            Headers headers = consumerRecord.headers();
            Header header = headers.lastHeader(CustomKafkaHeaders.IDEMPOTENCY_KEY);

            if (Objects.isNull(header)) {
                log.warn("Kafka message can't be processed (Idempotency key is not exists). Message: {}", consumerRecord);
                return false;
            }

            boolean messageAlreadyConsumed = redisKafkaManager.isMessageConsumed(
                    new String(header.value(), StandardCharsets.UTF_8)
            );

            if (messageAlreadyConsumed) {
                log.warn("Kafka message can't be processed (Already consumed). Message: {}", consumerRecord);
            } else {
                log.debug("Kafka message will be processed. Message: {}", consumerRecord);
            }

            return messageAlreadyConsumed;
        };
    }

    private class CustomDeadLetterPublishingRecoverer extends DeadLetterPublishingRecoverer {
        public CustomDeadLetterPublishingRecoverer(KafkaTemplate<?, ?> template, BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> destinationResolver) {
            super(template, destinationResolver);
        }

        @Override
        protected void publish(ProducerRecord<Object, Object> outRecord, KafkaOperations<Object, Object> kafkaTemplate) {
            try {
                kafkaTemplate.send(outRecord).get();
                Headers headers = outRecord.headers();
                Header header = headers.lastHeader(CustomKafkaHeaders.IDEMPOTENCY_KEY);
                redisKafkaManager.messageConsumed(
                        new String(header.value(), StandardCharsets.UTF_8)
                );
                log.warn("Sent kafka message to Dead-letter queue. Message: {}", outRecord);
            } catch (Exception e) {
                log.error("Dead-letter publication failed. Message: {}", outRecord);
                throw new RuntimeException(e);
            }
        }

        @Override
        public void accept(ConsumerRecord<?, ?> record, Exception exception) {
            super.accept(record, exception);

            log.error(exception.getMessage(), exception);
        }
    }
}