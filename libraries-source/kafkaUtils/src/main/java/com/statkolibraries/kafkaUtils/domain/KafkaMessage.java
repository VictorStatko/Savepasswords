package com.statkolibraries.kafkaUtils.domain;

import com.statkolibraries.kafkaUtils.enums.AccountKafkaActions;

public class KafkaMessage {
    private AccountKafkaActions action;

    private String payload;

    public KafkaMessage() {
    }

    public KafkaMessage(AccountKafkaActions action) {
        this.action = action;
    }

    public KafkaMessage(AccountKafkaActions action, String payload) {
        this.action = action;
        this.payload = payload;
    }

    public AccountKafkaActions getAction() {
        return action;
    }

    public void setAction(AccountKafkaActions action) {
        this.action = action;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
