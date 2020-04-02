package com.statkovit.personalAccountsService.unit.encryptors;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.utils.AesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.account;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.accountDTO;

@ExtendWith(MockitoExtension.class)
class PersonalAccountsEncryptorUTest {

    @Mock
    private AesUtils aesUtils;

    @InjectMocks
    private PersonalAccountsEncryptor personalAccountsEncryptor;

    @Test
    void encryptFieldsShouldSetNewSaltIfNotExists() {
        final PersonalAccount account = account();

        when(aesUtils.generateSalt()).thenReturn("new salt");

        personalAccountsEncryptor.encryptFields(account);

        assertNotNull(account.getFieldsEncryptionSalt());
        assertEquals(account.getFieldsEncryptionSalt(), "new salt");
    }

    @Test
    void encryptFieldsShouldNotSetNewSaltIfAlreadyExists() {
        final PersonalAccount account = account();
        account.setFieldsEncryptionSalt("old salt");

        when(aesUtils.generateSalt()).thenReturn("new salt");

        personalAccountsEncryptor.encryptFields(account);

        assertNotNull(account.getFieldsEncryptionSalt());
        assertEquals(account.getFieldsEncryptionSalt(), "old salt");
    }

    @Test
    void encryptFieldsShouldChangeNotNullFields() {
        final PersonalAccount account = account();
        account.setFieldsEncryptionSalt("salt");
        account.setPassword("decrypted password");
        account.setUsername("decrypted username");

        when(aesUtils.encrypt(account.getPassword(), account.getFieldsEncryptionSalt()))
                .thenReturn("encrypted password");
        when(aesUtils.encrypt(account.getUsername(), account.getFieldsEncryptionSalt()))
                .thenReturn("encrypted username");

        personalAccountsEncryptor.encryptFields(account);

        assertNotNull(account.getPassword());
        assertEquals(account.getPassword(), "encrypted password");
        assertNotNull(account.getUsername());
        assertEquals(account.getUsername(), "encrypted username");
    }

    @Test
    void decryptFieldsShouldChangeNotNullFields() {
        final PersonalAccountDto accountDto = accountDTO();
        accountDto.setPassword("encrypted password");
        accountDto.setUsername("encrypted username");

        when(aesUtils.decrypt(accountDto.getPassword(), "salt"))
                .thenReturn("decrypted password");
        when(aesUtils.decrypt(accountDto.getUsername(), "salt"))
                .thenReturn("decrypted username");

        personalAccountsEncryptor.decryptFields("salt", accountDto);

        assertNotNull(accountDto.getPassword());
        assertEquals(accountDto.getPassword(), "decrypted password");
        assertNotNull(accountDto.getUsername());
        assertEquals(accountDto.getUsername(), "decrypted username");
    }
}