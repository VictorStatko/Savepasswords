package com.statkovit.userservice.kafka;

import com.statkolibraries.kafkaUtils.enums.CustomKafkaHeaders;
import com.statkovit.userservice.domain.OutboxEvent;
import com.statkovit.userservice.repository.OutboxEventRepository;
import com.statkovit.userservice.util.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KafkaOutboxEventSender {

    private static final int MAX_EVENT_COUNT_FOR_PROCESSING = 30;

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TransactionUtils transactionUtils;

    /**
     * Garanties: No data will be deleted from 'Outbox' table before successful producing of messages
     * In case of successful Kafka transaction but failed JPA transaction the same message can be sent several times
     * Each consumer should check Idempotency-Key and discard message if it was already processed
     */

    @Scheduled(fixedRateString = "${custom.kafka.outbox.fixedRateInMs}")
    public void processOutboxEvents() {

        final List<OutboxEvent> outboxEvents = outboxEventRepository.findAll(
                PageRequest.of(0, MAX_EVENT_COUNT_FOR_PROCESSING, Sort.by(Sort.Direction.ASC, "id"))
        ).getContent();

        if (CollectionUtils.isNotEmpty(outboxEvents)) {
            kafkaTemplate.executeInTransaction(kafkaOperations -> {
                outboxEvents.forEach(event -> {
                    MessageBuilder<String> messageBuilder = MessageBuilder
                            .withPayload(event.getPayload())
                            .setHeader(KafkaHeaders.TOPIC, event.getTopic())
                            .setHeader(CustomKafkaHeaders.IDEMPOTENCY_KEY.getHeader(), event.getIdempotencyKey());

                    if (StringUtils.isNotEmpty(event.getMessageKey())) {
                        messageBuilder.setHeader(KafkaHeaders.MESSAGE_KEY, event.getMessageKey());
                    }

                    if (Objects.nonNull(event.getPartition())) {
                        messageBuilder.setHeader(KafkaHeaders.PARTITION_ID, event.getPartition());
                    }

                    final Message<String> message = messageBuilder.build();

                    kafkaOperations.send(message);
                });

                //noinspection ConstantConditions
                return null;
            });

            transactionUtils.executeInTransactionWithoutResult(
                    () -> outboxEventRepository.deleteAllByIdIsLessThanEqual(outboxEvents.get(outboxEvents.size() - 1).getId())
            );
        }

    }
}
