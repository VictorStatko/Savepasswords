package com.statkolibraries.kafkaUtils.domain;

import com.statkolibraries.kafkaUtils.enums.KafkaActions;

public class KafkaMessage {
    private KafkaActions action;

    private String payload;

    public KafkaMessage() {
    }

    public KafkaMessage(KafkaActions action) {
        this.action = action;
    }

    public KafkaMessage(KafkaActions action, String payload) {
        this.action = action;
        this.payload = payload;
    }

    public KafkaActions getAction() {
        return action;
    }

    public void setAction(KafkaActions action) {
        this.action = action;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
