package com.statkolibraries.kafkaUtils;

public final class KafkaTopics {

    public static final class Users {
        public static final String TOPIC_NAME = "users";
    }

    public static final class Accounts {
        public static final String TOPIC_NAME = "accounts";
        public static final String FAILURES_TOPIC_NAME = TOPIC_NAME + ".failures";
    }
}
