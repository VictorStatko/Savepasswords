package com.statkovit.authorizationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SequenceGenerator(name = "default_gen", sequenceName = "outbox_event_id_seq", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "message_key")
    private String messageKey;

    @Column(name = "partition")
    private Integer partition;

    @Type(type = "pg-uuid")
    @Column(name = "idempotency_key", nullable = false, updatable = false)
    private UUID idempotencyKey;

    @PrePersist
    private void initializeIdempotencyKey() {
        if (idempotencyKey == null) {
            idempotencyKey = UUID.randomUUID();
        }
    }

    public OutboxEvent() {
    }

    public OutboxEvent(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public OutboxEvent(String topic, String payload, Integer partition) {
        this(topic, payload);
        this.payload = payload;
        this.partition = partition;
    }

    public OutboxEvent(String topic, String payload, String messageKey) {
        this(topic, payload);
        this.messageKey = messageKey;
    }

    public OutboxEvent(String topic, String payload, String messageKey, Integer partition) {
        this(topic, payload, messageKey);
        this.partition = partition;
    }
}
