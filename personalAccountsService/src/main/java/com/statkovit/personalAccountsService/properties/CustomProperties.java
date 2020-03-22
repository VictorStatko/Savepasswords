package com.statkovit.personalAccountsService.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
@Getter
@Setter
public class CustomProperties {

    private Aes aes;

    @Getter
    @Setter
    public static final class Aes {
        private String key;
    }
}
