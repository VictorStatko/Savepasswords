package com.statkovit.authorizationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(value = "app.jwe")
@Getter
@Setter
public class JweProperties {

    private String senderPrivateKey;

    private String recipientPublicKey;

    private String senderPublicKey;

    private String recipientPrivateKey;

    private Long accessTokenExpiration;

    private Long refreshTokenExpiration;

}
