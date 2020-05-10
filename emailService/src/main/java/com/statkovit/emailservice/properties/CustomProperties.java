package com.statkovit.emailservice.properties;


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
    private Aws aws;
    private Email email;
    private Frontend frontend;

    @Getter
    @Setter
    public static final class Kafka {
        private Producer producer;
        private String trustStoreLocation;
        private String keyStoreLocation;
        private String password;
        private String clientUsername;
        private String clientPassword;

        @Getter
        @Setter
        public static final class Producer {
            private String transactionIdPrefix;
        }
    }

    @Getter
    @Setter
    public static final class Aws {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static final class Email {
        private String from;
    }

    @Getter
    @Setter
    public static final class Frontend {
        private String url;
    }
}
