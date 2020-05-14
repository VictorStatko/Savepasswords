package com.statkovit.personalAccountsService.encryptors;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.utils.AesUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonalAccountsEncryptor {

    private final AesUtils aesUtils;

    public void encryptFields(PersonalAccount account) {
        final String salt = Optional.ofNullable(account.getFieldsEncryptionSalt())
                .orElse(aesUtils.generateSalt());

        account.setFieldsEncryptionSalt(salt);

        account.setPassword(
                Optional.ofNullable(account.getPassword())
                        .filter(StringUtils::isNotBlank)
                        .map(password -> aesUtils.encrypt(password, salt))
                        .orElse(null)
        );

        account.setUsername(
                Optional.ofNullable(account.getUsername())
                        .filter(StringUtils::isNotBlank)
                        .map(username -> aesUtils.encrypt(username, salt))
                        .orElse(null)
        );

        account.setDescription(
                Optional.ofNullable(account.getDescription())
                        .filter(StringUtils::isNotBlank)
                        .map(description -> aesUtils.encrypt(description, salt))
                        .orElse(null)
        );

        account.setEncryptedAesClientKey(
                Optional.ofNullable(account.getEncryptedAesClientKey())
                        .filter(StringUtils::isNotBlank)
                        .map(key -> aesUtils.encrypt(key, salt))
                        .orElse(null)
        );
    }

    public void decryptFields(String salt, PersonalAccountDto dto) {
        dto.setUsername(
                Optional.ofNullable(dto.getUsername())
                        .filter(StringUtils::isNotBlank)
                        .map(username -> aesUtils.decrypt(username, salt))
                        .orElse(null)
        );

        dto.setPassword(
                Optional.ofNullable(dto.getPassword())
                        .filter(StringUtils::isNotBlank)
                        .map(password -> aesUtils.decrypt(password, salt))
                        .orElse(null)
        );

        dto.setDescription(
                Optional.ofNullable(dto.getDescription())
                        .filter(StringUtils::isNotBlank)
                        .map(description -> aesUtils.decrypt(description, salt))
                        .orElse(null)
        );

        dto.setEncryptedAesClientKey(
                Optional.ofNullable(dto.getEncryptedAesClientKey())
                        .filter(StringUtils::isNotBlank)
                        .map(key -> aesUtils.decrypt(key, salt))
                        .orElse(null)
        );
    }
}
