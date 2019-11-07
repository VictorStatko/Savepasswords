package com.statkolibraries.jwtprocessing;

import com.statkolibraries.jwtprocessing.exception.ExpiredJwtException;
import com.statkolibraries.jwtprocessing.exception.TokenProcessingException;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class JwsProcessor {

    private final String publicKeyBase64Encoded;

    public JwsProcessor(String publicKeyBase64Encoded) {
        this.publicKeyBase64Encoded = publicKeyBase64Encoded;
    }

    public TokenData readToken(String token) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyBase64Encoded));
            KeyFactory keyFactory = KeyFactory.getInstance(SignatureAlgorithm.RS512.getFamilyName());
            PublicKey publicKey = keyFactory.generatePublic(spec);

            Claims jws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            return TokenData.fromClaimsMap(jws);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getMessage(), e.getCause());
        } catch (JwtException e) {
            throw new TokenProcessingException(e.getMessage(), e.getCause());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
