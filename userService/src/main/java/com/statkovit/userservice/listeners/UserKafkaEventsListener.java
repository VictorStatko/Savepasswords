package com.statkovit.userservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.kafkaUtils.domain.KafkaMessage;
import com.statkolibraries.kafkaUtils.enums.KafkaActions;
import com.statkolibraries.kafkaUtils.enums.KafkaTopics;
import com.statkovit.userservice.domain.OutboxEvent;
import com.statkovit.userservice.dto.UserDto;
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
        KafkaMessage<UserDto> kafkaMessage = new KafkaMessage<>(KafkaActions.USER_CREATED, userCreatedEvent.getUserDto());

        OutboxEvent outboxEvent = new OutboxEvent(
                KafkaTopics.USERS.getTopicName(),
                objectMapper.writeValueAsString(kafkaMessage),
                userCreatedEvent.getUserUuid().toString()
        );

        outboxEventRepository.save(outboxEvent);
    }
}
