package com.statkovit.emailservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Log4j2
public class RedisKafkaManager {

    private final RedissonClient redissonClient;

    private static final String SET_NAME = "consumedMessages";

    public void messageConsumedSafely(String idempotencyKey) {
        try {
            RSetCache<String> set = redissonClient.getSetCache(SET_NAME);
            set.add(idempotencyKey, 7, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean isMessageConsumedSafely(String idempotencyKey) {
        try {
            RSetCache<String> set = redissonClient.getSetCache(SET_NAME);
            return set.contains(idempotencyKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
