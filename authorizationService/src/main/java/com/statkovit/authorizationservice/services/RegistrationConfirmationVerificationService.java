package com.statkovit.authorizationservice.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.authorizationservice.properties.CustomProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegistrationConfirmationVerificationService {

    private static final String MAP_NAME = "registration_confirmation_codes";

    private final RedissonClient redissonClient;
    private final CustomProperties customProperties;

    public VerificationCode createNewVerificationCode(Long accountId) {
        RMapCache<String, String> map = redissonClient.getMapCache(MAP_NAME);

        int expiration = customProperties.getRegistration().getConfirmationTokenExpirationInHours();

        String verificationCode = new SecuredRandomStringGenerator(8).generate();

        map.fastPut(accountId.toString(), verificationCode, expiration, TimeUnit.HOURS);

        return new VerificationCode(verificationCode, expiration);
    }

    public Long confirmRegistration(String verificationCode) {
        RMapCache<String, String> map = redissonClient.getMapCache(MAP_NAME);

        String accountId = map.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), verificationCode))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() ->
                        new LocalizedException(
                                String.format("Registration confirmation verificationCode %s is not found.", verificationCode),
                                "exceptions.registrationTokenNotFound"
                        )
                );

        map.fastRemove(accountId);

        return Long.valueOf(accountId);
    }

    @Getter
    @Service
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class VerificationCode {
        private String verificationCode;
        private int expirationInHours;
    }
}
