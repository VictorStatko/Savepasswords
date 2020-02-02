package com.statkolibraries.kafkaUtils.enums;

public enum KafkaTopics {
    USERS("users");

    private final String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
