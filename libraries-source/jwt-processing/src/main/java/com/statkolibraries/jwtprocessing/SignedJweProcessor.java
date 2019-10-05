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

public final class SignedJweProcessor {

    private final String senderPublicKey;
    private final String recipientPrivateKey;

    public SignedJweProcessor(String senderPublicKey, String recipientPrivateKey) {
        this.senderPublicKey = senderPublicKey;
        this.recipientPrivateKey = recipientPrivateKey;
    }

    public TokenData processJWE(String jwe) throws ParseException, JOSEException, net.minidev.json.parser.ParseException {
        RSAKey recipientPrivateRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(recipientPrivateKey)));
        RSAKey senderPublicRSAKey = RSAKey.parse(new String(Base64.getDecoder().decode(senderPublicKey)));

        JWEObject jweObject = JWEObject.parse(jwe);

        jweObject.decrypt(new RSADecrypter(recipientPrivateRSAKey));

        SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

        signedJWT.verify(new RSASSAVerifier(senderPublicRSAKey));

        JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

        JSONObject json = (JSONObject) jsonParser.parse(
                signedJWT.getJWTClaimsSet().getClaim(SignedJweCreator.USER_DATA_CLAIM).toString()
        );

        return TokenData.fromJson(json);

    }
}
