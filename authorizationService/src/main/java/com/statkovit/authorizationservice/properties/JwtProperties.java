package com.statkovit.authorizationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(value = "app.jwt")
@Getter
@Setter
public class JwtProperties {

    private String privateKey;

    private String publicKey;

    private Long accessTokenExpiration;

    private Long refreshTokenExpiration;

}
