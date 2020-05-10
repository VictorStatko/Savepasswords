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

    private IpStack ipstack;

    private Registration registration;

    @Getter
    @Setter
    public static final class Kafka {
        private Outbox outbox;
        private Producer producer;
        private String trustStoreLocation;
        private String keyStoreLocation;
        private String password;
        private String clientUsername;
        private String clientPassword;


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


    @Getter
    @Setter
    public static final class IpStack {
        private String accessKey;
    }

    @Getter
    @Setter
    public static final class Registration {
        private int confirmationTokenExpirationInHours;
    }
}
