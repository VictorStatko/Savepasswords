package com.statkovit.personalAccountsService.utils;

import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.personalAccountsService.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AesUtils {

    private final CustomProperties customProperties;


    public String encrypt(String data, String salt) {
        TextEncryptor privateKeyEncryptor = getEncryptor(salt);

        return privateKeyEncryptor.encrypt(data);
    }

    public String decrypt(String data, String salt) {
        TextEncryptor privateKeyEncryptor = getEncryptor(salt);

        return privateKeyEncryptor.decrypt(data);
    }

    public String generateSalt() {
        return new String(
                Hex.encode(new SecuredRandomStringGenerator(16).generate().getBytes())
        );
    }

    private TextEncryptor getEncryptor(String salt) {
        return Encryptors.text(
                new String(Base64.getDecoder().decode(customProperties.getAes().getKey())),
                salt
        );
    }
}
