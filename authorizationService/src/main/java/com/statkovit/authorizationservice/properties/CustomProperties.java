package com.statkovit.authorizationservice.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
@Getter
@Setter
public class CustomProperties {

    private Kafka kafka;

    private Aes aes;

    @Getter
    @Setter
    public static final class Kafka {
        private Outbox outbox;
        private Producer producer;


        @Getter
        @Setter
        public static final class Outbox {
            private Long fixedRateInMs;
        }

        @Getter
        @Setter
        public static final class Producer {
            private String transactionIdPrefix;
        }
    }


    @Getter
    @Setter
    public static final class Aes {
        private String key;
    }
}
