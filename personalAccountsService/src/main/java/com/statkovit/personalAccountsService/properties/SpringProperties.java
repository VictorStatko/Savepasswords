package com.statkovit.personalAccountsService.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring")
@Getter
@Setter
public class SpringProperties {

    private Redis redis;

    @Getter
    @Setter
    public static final class Redis {
        private String clusters;
        private String password;
    }

}

