package com.statkovit.userservice.kafka;

import com.statkolibraries.kafkaUtils.enums.KafkaTopics;
import com.statkovit.userservice.domain.OutboxEvent;
import com.statkovit.userservice.repository.OutboxEventRepository;
import com.statkovit.userservice.util.TransactionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaOutboxEventSender {

    private static final int MAX_EVENT_COUNT_FOR_PROCESSING = 10;

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TransactionUtils transactionUtils;

    @Autowired
    @Qualifier("lastProcessedEventConsumer")
    private Consumer<String, String> lastProcessedEventConsumer;

    @Scheduled(fixedRateString = "${custom.kafka.outbox.fixedRateInMs}")
    public void scheduleEventsSendTask() {

        log.debug("Started EventsSendTask.");

        TopicPartition lastProcessedEventPartition = new TopicPartition(
                KafkaTopics.USER_SERVICE_LAST_PROCESSED_EVENTS.getTopicName(), 0
        );

        lastProcessedEventConsumer.seekToEnd(
                Collections.singletonList(lastProcessedEventPartition)
        );

        long lastMessagePosition = lastProcessedEventConsumer.position(lastProcessedEventPartition);

        lastProcessedEventConsumer.seek(
                lastProcessedEventPartition, lastMessagePosition == 0 ? lastMessagePosition : lastMessagePosition - 1
        );

        ConsumerRecords<String, String> lastProcessedMessages = lastProcessedEventConsumer.poll(Duration.of(100, ChronoUnit.MILLIS));

        Long lastId;

        if (lastProcessedMessages.isEmpty()) {
            lastId = 0L;
        } else {
            ConsumerRecord<String, String> lastProcessedEventId = lastProcessedMessages.iterator().next();
            lastId = Long.parseLong(lastProcessedEventId.value());
        }

        List<OutboxEvent> eventsForProcess = outboxEventRepository.findAllByIdIsGreaterThan(lastId,
                PageRequest.of(0, MAX_EVENT_COUNT_FOR_PROCESSING, Sort.by(Sort.Direction.ASC, "id"))
        ).getContent();

        if (CollectionUtils.isNotEmpty(eventsForProcess)) {
            kafkaTemplate.executeInTransaction(kafkaOperations -> {

                eventsForProcess.forEach(event -> kafkaOperations.send(event.getTopic(), event.getPayload()));

                kafkaOperations.send(
                        KafkaTopics.USER_SERVICE_LAST_PROCESSED_EVENTS.getTopicName(),
                        eventsForProcess.get(eventsForProcess.size() - 1).getId()
                );
                return null;
            });

            transactionUtils.executeInTransactionWithoutResult(
                    () -> outboxEventRepository.deleteAllByIdIsLessThanEqual(eventsForProcess.get(eventsForProcess.size() - 1).getId())
            );

        }

        log.debug("Finished EventsSendTask.");

    }
}
