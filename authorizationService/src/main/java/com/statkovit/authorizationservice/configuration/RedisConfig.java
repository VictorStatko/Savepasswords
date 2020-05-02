package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.configuration.keyGenerator.RedisAuthenticationKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@SuppressWarnings("deprecation")
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisAuthenticationKeyGenerator authenticationKeyGenerator;

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setAuthenticationKeyGenerator(authenticationKeyGenerator);
        return redisTokenStore;
    }
}
