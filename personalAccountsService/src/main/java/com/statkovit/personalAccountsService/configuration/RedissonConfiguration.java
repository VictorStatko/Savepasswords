package com.statkovit.personalAccountsService.configuration;

import com.statkovit.personalAccountsService.properties.SpringProperties;
import com.statkovit.personalAccountsService.properties.SpringProperties.Redis;
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
    private static final int SCAN_INTERVAL = 2000;

    @Bean
    public RedissonClient getRedisson() {
        final Redis redis = springProperties.getRedis();

        final String[] nodes = redis.getClusters().split(",");

        if (nodes.length == 0) {
            throw new IllegalArgumentException("At least one node should be provided");
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = "redis://" + nodes[i];
        }

        final Config config = new Config();

        if (nodes.length > 1) {
            config.useClusterServers()
                    .setScanInterval(SCAN_INTERVAL)
                    .addNodeAddress(nodes)
                    .setRetryAttempts(RETRY_ATTEMPTS)
                    .setTimeout(TIMEOUT)
                    .setPassword(redis.getPassword());
        } else {
            config.useSingleServer().setAddress(nodes[0])
                    .setRetryAttempts(RETRY_ATTEMPTS)
                    .setTimeout(TIMEOUT)
                    .setPassword(redis.getPassword());
        }

        return Redisson.create(config);
    }
}
