package com.statkolibraries.jwtprocessing;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.statkolibraries.jwtprocessing.payload.TokenData;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class SignedJweCreator {

    public static final String USER_DATA_CLAIM = "userData";
    private static final String JWE_CONTENT_TYPE = "JWT";

    private final String senderPrivateKey;
    private final String recipientPublicKey;
    private final long expirationInMs;

    public SignedJweCreator(String senderPrivateKey, String recipientPublicKey, long expirationInMs) {
        this.senderPrivateKey = senderPrivateKey;
        this.recipientPublicKey = recipientPublicKey;
        this.expirationInMs = expirationInMs;
    }

    public String generateAccessToken(TokenData tokenData) throws ParseException, JOSEException {
        SignedJWT signedJWT = createAndSignJWT(tokenData.getUuid(), tokenData);

        JWEObject jweObject = createAndEncryptJWE(signedJWT);

        return jweObject.serialize();
    }

    public String generateRefreshToken(UUID userUuid) throws ParseException, JOSEException {
        SignedJWT signedJWT = createAndSignJWT(userUuid, null);

        JWEObject jweObject = createAndEncryptJWE(signedJWT);

        return jweObject.serialize();
    }

    private SignedJWT createAndSignJWT(UUID userUuid, TokenData tokenData) throws JOSEException, ParseException {
        RSAKey senderPrivateRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(senderPrivateKey)));

        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).build();

        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet
                .Builder()
                .subject(userUuid.toString())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationInMs));

        if (Objects.nonNull(tokenData)) {
            claimsSetBuilder = claimsSetBuilder.claim(USER_DATA_CLAIM, tokenData.toJSON());
        }

        JWTClaimsSet claimsSet = claimsSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);

        signedJWT.sign(new RSASSASigner(senderPrivateRSAKey));

        return signedJWT;
    }

    private JWEObject createAndEncryptJWE(SignedJWT payload) throws JOSEException, ParseException {
        RSAKey recipientPublicRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(recipientPublicKey)));

        JWEHeader jweHeader = new JWEHeader
                .Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                .contentType(JWE_CONTENT_TYPE)
                .build();

        Payload jwePayload = new Payload(payload);

        JWEObject jweObject = new JWEObject(jweHeader, jwePayload);

        jweObject.encrypt(new RSAEncrypter(recipientPublicRSAKey));

        return jweObject;
    }
}
