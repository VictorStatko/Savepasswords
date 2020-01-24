package com.statkovit.userservice.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private Kafka kafka;

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public static final class Kafka {
        private Outbox outbox;
        private Producer producer;

        public Outbox getOutbox() {
            return outbox;
        }

        public Producer getProducer() {
            return producer;
        }

        public void setOutbox(Outbox outbox) {
            this.outbox = outbox;
        }

        public void setProducer(Producer producer) {
            this.producer = producer;
        }

        public static final class Outbox {
            private Long fixedRateInMs;

            public Long getFixedRateInMs() {
                return fixedRateInMs;
            }

            public void setFixedRateInMs(Long fixedRateInMs) {
                this.fixedRateInMs = fixedRateInMs;
            }
        }

        public static final class Producer {
            private String transactionIdPrefix;

            public String getTransactionIdPrefix() {
                return transactionIdPrefix;
            }

            public void setTransactionIdPrefix(String transactionIdPrefix) {
                this.transactionIdPrefix = transactionIdPrefix;
            }
        }
    }
}
