package com.statkolibraries.kafkaUtils.enums;

public enum KafkaTopics {
    USERS("users"),
    USER_SERVICE_LAST_PROCESSED_EVENTS("user_service_last_processed_events");

    private final String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
