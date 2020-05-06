package com.statkovit.emailservice.kafka.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.kafkaUtils.CustomKafkaHeaders;
import com.statkolibraries.kafkaUtils.KafkaTopics;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkovit.emailservice.configuration.KafkaConfiguration;
import com.statkovit.emailservice.kafka.RedisKafkaManager;
import com.statkovit.emailservice.payload.AccountVerificationRequestedDto;
import com.statkovit.emailservice.services.AccountEmailService;
import com.statkovit.emailservice.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class AccountListener {

    private final RedisKafkaManager redisKafkaManager;
    private final ObjectMapperUtils objectMapperUtils;
    private final AccountEmailService accountEmailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = KafkaTopics.Accounts.TOPIC_NAME,
            containerFactory = KafkaConfiguration.LISTENER_FACTORY_NAME
    )
    @SneakyThrows
    public void onAccountDataMessage(
            @Payload KafkaMessage message,
            @Header(CustomKafkaHeaders.IDEMPOTENCY_KEY) String idempotencyKey
    ) {
        logMessageReceive(message, idempotencyKey);

        switch (message.getAction()) {
            case ACCOUNT_VERIFICATION_REQUESTED:
                accountEmailService.sendAccountVerificationEmail(
                        objectMapper.readValue(message.getPayload(), AccountVerificationRequestedDto.class)
                );
                break;
        }

        redisKafkaManager.messageConsumedSafely(idempotencyKey);
    }

    private void logMessageReceive(KafkaMessage message, String idempotencyKey) {
        log.debug("Kafka message received: {}", objectMapperUtils.safelyConvertObjectToString(message));
        log.debug("Kafka header received: {}", idempotencyKey);
    }
}
