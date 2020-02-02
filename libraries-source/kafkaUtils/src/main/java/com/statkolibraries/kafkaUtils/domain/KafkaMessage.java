package com.statkolibraries.kafkaUtils.domain;

import com.statkolibraries.kafkaUtils.enums.KafkaActions;

public class KafkaMessage<T> {
    private KafkaActions action;

    private T payload;

    public KafkaMessage(KafkaActions action) {
        this.action = action;
    }

    public KafkaMessage(KafkaActions action, T payload) {
        this(action);
        this.payload = payload;
    }

    public KafkaActions getAction() {
        return action;
    }

    public void setAction(KafkaActions action) {
        this.action = action;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
