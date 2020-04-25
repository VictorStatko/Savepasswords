package com.statkovit.userservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.kafkaUtils.KafkaTopics;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkolibraries.kafkaUtils.enums.KafkaActions;
import com.statkovit.userservice.domain.OutboxEvent;
import com.statkovit.userservice.events.UserCreatedEvent;
import com.statkovit.userservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserKafkaEventsListener {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    @SneakyThrows(IOException.class)
    public void onUserCreated(UserCreatedEvent userCreatedEvent) {
        KafkaMessage kafkaMessage = new KafkaMessage(KafkaActions.USER_CREATED, "qq");

        OutboxEvent outboxEvent = new OutboxEvent(
                KafkaTopics.Users.TOPIC_NAME,
                objectMapper.writeValueAsString(kafkaMessage),
                userCreatedEvent.getUserUuid().toString()
        );

        outboxEventRepository.save(outboxEvent);
    }
}
