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

        account.setDescription(aesUtils.encrypt(account.getDescription(), salt));
        account.setName(aesUtils.encrypt(account.getName(), salt));
        account.setPassword(aesUtils.encrypt(account.getPassword(), salt));
        account.setUsername(aesUtils.encrypt(account.getUsername(), salt));
        account.setUrl(aesUtils.encrypt(account.getUrl(), salt));
    }

    public void decryptFields(String salt, PersonalAccountDto dto) {
        dto.setDescription(aesUtils.decrypt(dto.getDescription(), salt));
        dto.setName(aesUtils.decrypt(dto.getName(), salt));
        dto.setPassword(aesUtils.decrypt(dto.getPassword(), salt));
        dto.setUrl(aesUtils.decrypt(dto.getUrl(), salt));
        dto.setUsername(aesUtils.decrypt(dto.getUsername(), salt));
    }
}
