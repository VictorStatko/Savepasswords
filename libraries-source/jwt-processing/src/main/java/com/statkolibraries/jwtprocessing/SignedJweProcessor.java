package com.statkolibraries.jwtprocessing;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.text.ParseException;
import java.util.Base64;
import java.util.UUID;

public final class SignedJweProcessor {

    private final String senderPublicKey;
    private final String recipientPrivateKey;

    public SignedJweProcessor(String senderPublicKey, String recipientPrivateKey) {
        this.senderPublicKey = senderPublicKey;
        this.recipientPrivateKey = recipientPrivateKey;
    }

    public TokenData processAccessJWE(String jwe) throws ParseException, JOSEException, net.minidev.json.parser.ParseException {

        SignedJWT signedJWT = processJWE(jwe);

        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        JSONObject json = (JSONObject) jsonParser.parse(
                signedJWT.getJWTClaimsSet().getClaim(SignedJweCreator.USER_DATA_CLAIM).toString()
        );

        return TokenData.fromJson(json);
    }

    public UUID processRefreshJWE(String jwe) throws ParseException, JOSEException {

        SignedJWT signedJWT = processJWE(jwe);

        return UUID.fromString(signedJWT.getJWTClaimsSet().getSubject());
    }

    private SignedJWT processJWE(String jwe) throws ParseException, JOSEException {
        RSAKey recipientPrivateRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(recipientPrivateKey)));
        RSAKey senderPublicRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(senderPublicKey)));

        JWEObject jweObject = JWEObject.parse(jwe);

        jweObject.decrypt(new RSADecrypter(recipientPrivateRSAKey));

        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

        signedJWT.verify(new RSASSAVerifier(senderPublicRSAKey));

        return signedJWT;
    }
}
