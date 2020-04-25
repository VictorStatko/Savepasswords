package com.statkovit.personalAccountsService.kafka;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisKafkaManager {

    private final RedissonClient redissonClient;

    private static final String SET_NAME = "consumedMessages";

    public void messageConsumed(String idempotencyKey) {
        RSetCache<String> set = redissonClient.getSetCache(SET_NAME);
        set.add(idempotencyKey, 7, TimeUnit.DAYS);
    }

    public boolean isMessageConsumed(String idempotencyKey) {
        RSetCache<String> set = redissonClient.getSetCache(SET_NAME);
        return set.contains(idempotencyKey);
    }

}
