package com.statkovit.authorizationservice.configuration.keyGenerator;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@SuppressWarnings("deprecation")
public class RedisAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private static final String CLIENT_ID = "client_id";

    private static final String TIME = "time";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<>();

        OAuth2Request authorizationRequest = authentication.getOAuth2Request();

        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }

        values.put(CLIENT_ID, authorizationRequest.getClientId());

        Optional.ofNullable(authorizationRequest.getScope()).ifPresent(scope ->
                values.put(SCOPE, OAuth2Utils.formatParameterList(scope))
        );

        values.put(TIME, String.valueOf(Instant.now().toEpochMilli()));

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available. Fatal (should be in the JDK).");
        }

        byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, bytes));
    }
}
