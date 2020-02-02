package com.statkolibraries.kafkaUtils.enums;

public enum CustomKafkaHeaders {

    IDEMPOTENCY_KEY("X-Idempotency-Key");

    private final String header;

    CustomKafkaHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
