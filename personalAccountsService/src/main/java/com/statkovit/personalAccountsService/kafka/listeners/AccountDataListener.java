package com.statkovit.personalAccountsService.kafka.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.kafkaUtils.CustomKafkaHeaders;
import com.statkolibraries.kafkaUtils.KafkaTopics;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkovit.personalAccountsService.configuration.KafkaConfiguration;
import com.statkovit.personalAccountsService.domainService.AccountDataDomainService;
import com.statkovit.personalAccountsService.kafka.RedisKafkaManager;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import com.statkovit.personalAccountsService.utils.ObjectMapperUtils;
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
public class AccountDataListener {

    private final RedisKafkaManager redisKafkaManager;
    private final ObjectMapperUtils objectMapperUtils;
    private final AccountDataDomainService accountDataDomainService;
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

        AccountDataDto dataDto = objectMapper.readValue(message.getPayload(), AccountDataDto.class);

        switch (message.getAction()) {
            case ACCOUNT_CREATED:
                accountDataDomainService.create(dataDto);
                break;
            case ACCOUNT_REMOVED:
                accountDataDomainService.remove(dataDto);
                break;
        }

        redisKafkaManager.messageConsumedSafely(idempotencyKey);
    }

    private void logMessageReceive(KafkaMessage message, String idempotencyKey) {
        log.debug("Kafka message received: {}", objectMapperUtils.safelyConvertObjectToString(message));
        log.debug("Kafka header received: {}", idempotencyKey);
    }
}
