package com.statkovit.personalAccountsService.encryptors;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.utils.AesUtils;
import lombok.RequiredArgsConstructor;
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

        account.setPassword(aesUtils.encrypt(account.getPassword(), salt));
        account.setUsername(aesUtils.encrypt(account.getUsername(), salt));
    }

    public void decryptFields(String salt, PersonalAccountDto dto) {
        dto.setPassword(aesUtils.decrypt(dto.getPassword(), salt));
        dto.setUsername(aesUtils.decrypt(dto.getUsername(), salt));
    }
}
