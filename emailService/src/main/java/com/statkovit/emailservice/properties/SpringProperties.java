package com.statkovit.emailservice.properties;

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
    private Profiles profiles;
    private Mail mail;

    @Getter
    @Setter
    public static final class Redis {
        private String host;
        private String port;
        private String password;
    }

    @Getter
    @Setter
    public static final class Profiles {
        private String active;
    }

    @Getter
    @Setter
    public static final class Mail {
        private String username;
    }

}

