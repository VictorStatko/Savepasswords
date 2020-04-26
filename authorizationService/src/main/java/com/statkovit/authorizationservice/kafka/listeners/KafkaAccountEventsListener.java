package com.statkovit.authorizationservice.kafka.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.kafkaUtils.KafkaTopics;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkolibraries.kafkaUtils.enums.AccountKafkaActions;
import com.statkovit.authorizationservice.entities.OutboxEvent;
import com.statkovit.authorizationservice.events.AccountCreatedEvent;
import com.statkovit.authorizationservice.repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaAccountEventsListener {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    @SneakyThrows(IOException.class)
    public void onAccountCreated(AccountCreatedEvent accountCreatedEvent) {
        KafkaMessage kafkaMessage = new KafkaMessage(
                AccountKafkaActions.ACCOUNT_CREATED, objectMapper.writeValueAsString(accountCreatedEvent.getAccount())
        );

        OutboxEvent outboxEvent = new OutboxEvent(
                KafkaTopics.Accounts.TOPIC_NAME,
                objectMapper.writeValueAsString(kafkaMessage),
                accountCreatedEvent.getAccount().getUuid().toString()
        );

        outboxEvent = outboxEventRepository.save(outboxEvent);

        logOutboxEvent(outboxEvent);
    }

    private void logOutboxEvent(OutboxEvent event) {
        try {
            log.debug("Saved new OutboxEvent: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage(), e);
        }
    }
}
