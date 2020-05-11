package com.statkovit.gatewayservice.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
@Getter
@Setter
public class CustomProperties {

    private Cookie cookie;
    private Frontend frontend;

    @Getter
    @Setter
    public static final class Cookie {
        private boolean secured;
    }

    @Getter
    @Setter
    public static final class Frontend {
        private String url;
    }
}
