package com.statkovit.emailservice.configuration;

import com.statkovit.emailservice.properties.SpringProperties;
import com.statkovit.emailservice.properties.SpringProperties.Redis;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfiguration {

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
