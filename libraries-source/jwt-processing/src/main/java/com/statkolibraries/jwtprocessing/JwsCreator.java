package com.statkolibraries.jwtprocessing;

import com.statkolibraries.jwtprocessing.payload.TokenData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

public final class JwsCreator {

    private final String privateKeyBase64Encoded;
    private final long expirationInMs;

    public JwsCreator(String privateKeyBase64Encoded, long expirationInMs) {
        this.privateKeyBase64Encoded = privateKeyBase64Encoded;
        this.expirationInMs = expirationInMs;
    }

    public String generateToken(TokenData tokenData) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyBase64Encoded));
            KeyFactory keyFactory = KeyFactory.getInstance(SignatureAlgorithm.RS512.getFamilyName());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            return Jwts.builder()
                    .setClaims(tokenData.toClaimMap())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                    .setIssuedAt(new Date())
                    .signWith(privateKey)
                    .compact();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
