package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.configuration.keyGenerator.RedisAuthenticationKeyGenerator;
import com.statkovit.authorizationservice.properties.SpringProperties;
import com.statkovit.authorizationservice.properties.SpringProperties.Redis;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
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

    private final SpringProperties springProperties;

    private static final int RETRY_ATTEMPTS = 5;
    private static final int TIMEOUT = 10000;

    @Bean
    public RedissonClient getRedisson() {
        final Redis redis = springProperties.getRedis();

        final Config config = new Config();

        config.useSingleServer().setAddress("redis://" + redis.getHost() + ":" + redis.getPort())
                .setRetryAttempts(RETRY_ATTEMPTS)
                .setTimeout(TIMEOUT)
                .setPassword(redis.getPassword());

        return Redisson.create(config);
    }
}
