package com.statkolibraries.jwtprocessing;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.statkolibraries.jwtprocessing.exception.ExpiredJwtException;
import com.statkolibraries.jwtprocessing.exception.TokenProcessingException;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public final class SignedJweProcessor {

    private static final String EXPIRED_JWT_MESSAGE = "Expired JWT";

    private final String senderPublicKey;
    private final String recipientPrivateKey;

    public SignedJweProcessor(String senderPublicKey, String recipientPrivateKey) {
        this.senderPublicKey = senderPublicKey;
        this.recipientPrivateKey = recipientPrivateKey;
    }

    public TokenData processAccessJWE(String jwe) throws TokenProcessingException {

        JWTClaimsSet claimsSet = processJWE(jwe);

        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        try {
            JSONObject json = (JSONObject) jsonParser.parse(
                    claimsSet.getClaim(SignedJweCreator.USER_DATA_CLAIM).toString()
            );

            return TokenData.fromJson(json);
        } catch (Exception e) {
            throw new TokenProcessingException(e.getMessage(), e.getCause());
        }

    }

    public UUID processRefreshJWE(String jwe) throws TokenProcessingException {

        JWTClaimsSet claimsSet = processJWE(jwe);

        try {
            return UUID.fromString(claimsSet.getSubject());
        } catch (IllegalArgumentException e) {
            throw new TokenProcessingException(e.getMessage(), e.getCause());
        }
    }

    private JWTClaimsSet processJWE(String jwe) throws TokenProcessingException {
        try {
            RSAKey recipientPrivateRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(recipientPrivateKey)));
            RSAKey senderPublicRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(senderPublicKey)));

            JWEObject jweObject = JWEObject.parse(jwe);

            jweObject.decrypt(new RSADecrypter(recipientPrivateRSAKey));

            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

            signedJWT.verify(new RSASSAVerifier(senderPublicRSAKey));

            ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
            JWKSource jwkSource = new ImmutableJWKSet(new JWKSet(senderPublicRSAKey));
            JWSAlgorithm expectedJWSALg = JWSAlgorithm.RS256;
            JWSKeySelector jwsKeySelector = new JWSVerificationKeySelector(expectedJWSALg, jwkSource);
            jwtProcessor.setJWSKeySelector(jwsKeySelector);

            return jwtProcessor.process(signedJWT, null);
        } catch (Exception exception) {
            if (Objects.equals(exception.getMessage(), EXPIRED_JWT_MESSAGE)) {
                throw new ExpiredJwtException(exception.getMessage(), exception.getCause());
            }
            throw new TokenProcessingException(exception.getMessage(), exception.getCause());
        }

    }
}
