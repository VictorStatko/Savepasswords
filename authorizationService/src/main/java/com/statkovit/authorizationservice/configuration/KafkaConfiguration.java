package com.statkovit.authorizationservice.configuration;

import com.statkolibraries.kafkaUtils.KafkaTopics;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkovit.authorizationservice.properties.CustomProperties;
import com.statkovit.authorizationservice.properties.CustomProperties.Kafka;
import com.statkovit.authorizationservice.utils.ProfilesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;
    private final CustomProperties customProperties;
    private final ProfilesManager profilesManager;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(
                ProducerConfig.TRANSACTIONAL_ID_CONFIG,
                customProperties.getKafka().getProducer().getTransactionIdPrefix()
        );

        if (profilesManager.isProduction()) {
            Kafka kafka = customProperties.getKafka();
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name);
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            props.put(
                    SaslConfigs.SASL_JAAS_CONFIG,
                    "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                            + kafka.getClientUsername()
                            + "\" password=\""
                            + kafka.getClientPassword()
                            + "\";"
            );
            props.put("ssl.truststore.location", kafka.getTrustStoreLocation());
            props.put("ssl.truststore.password", kafka.getPassword());
            props.put("ssl.key.password", kafka.getPassword());
            props.put("ssl.keystore.password", kafka.getPassword());
            props.put("ssl.keystore.location", kafka.getKeyStoreLocation());
        }

        return props;
    }

    @Bean
    public ProducerFactory<String, KafkaMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildAdminProperties());

        if (profilesManager.isProduction()) {
            Kafka kafka = customProperties.getKafka();
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name);
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            props.put(
                    SaslConfigs.SASL_JAAS_CONFIG,
                    "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                            + kafka.getClientUsername()
                            + "\" password=\""
                            + kafka.getClientPassword()
                            + "\";"
            );
            props.put("ssl.truststore.location", kafka.getTrustStoreLocation());
            props.put("ssl.truststore.password", kafka.getPassword());
            props.put("ssl.key.password", kafka.getPassword());
            props.put("ssl.keystore.password", kafka.getPassword());
            props.put("ssl.keystore.location", kafka.getKeyStoreLocation());
        }

        KafkaAdmin kafkaAdmin = new KafkaAdmin(props);
        kafkaAdmin.setFatalIfBrokerNotAvailable(kafkaProperties.getAdmin().isFailFast());

        return kafkaAdmin;
    }

    @Bean
    public NewTopic accountsTopic() {
        return TopicBuilder.name(KafkaTopics.Accounts.TOPIC_NAME)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic accountsFailureTopic() {
        return TopicBuilder.name(KafkaTopics.Accounts.FAILURES_TOPIC_NAME)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager transactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
}
