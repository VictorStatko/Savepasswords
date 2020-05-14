package com.statkovit.personalAccountsService.unit.payload.converters;

import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountConverter;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.account;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.accountDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountConverterUTest {

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final Long ID_1 = 1L;

    @Mock
    private PersonalAccountsEncryptor personalAccountsEncryptor;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private PersonalAccountFolderService folderService;

    @Mock
    private AccountDataService accountDataService;

    @InjectMocks
    private PersonalAccountConverter personalAccountConverter;

    @Test
    void toEntityConverterShouldSetAccountEntityIdForNewAccount() {
        final PersonalAccount accountForUpdate = account();
        final PersonalAccountDto dtoForUpdate = accountDto();

        Mockito.when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_1);
        Mockito.when(accountDataService.internalGetById(ID_1)).thenReturn(new AccountData().toBuilder().id(ID_1).build());

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertEquals(ID_1, accountForUpdate.getAccountEntityId());
        Assertions.assertEquals(ID_1, accountForUpdate.getDuplicatedAccountEntity().getId());
    }

    @Test
    void toEntityConverterShouldNotUpdateAccountEntityIdForOldAccount() {
        final PersonalAccount accountForUpdate = new PersonalAccount().toBuilder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        final PersonalAccountDto dtoForUpdate = new PersonalAccountDto().toBuilder()
                .uuid(UUID_1).build();

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertEquals(ID_1, accountForUpdate.getAccountEntityId());
    }

    @Test
    void toEntityConverterShouldUpdateMainFields() {
        Mockito.when(accountDataService.internalGetById(any())).thenReturn(new AccountData());

        final PersonalAccount accountForUpdate = new PersonalAccount().toBuilder().build();

        final PersonalAccountDto dtoForUpdate = new PersonalAccountDto().toBuilder()
                .uuid(UUID_1)
                .url("url")
                .name("name")
                .username("username")
                .password("password")
                .description("description")
                .encryptedAesClientKey("aesKey")
                .build();

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertNull(accountForUpdate.getUuid());
        Assertions.assertEquals("url", accountForUpdate.getUrl());
        Assertions.assertEquals("name", accountForUpdate.getName());
        Assertions.assertEquals("username", accountForUpdate.getUsername());
        Assertions.assertEquals("password", accountForUpdate.getPassword());
        Assertions.assertEquals("aesKey", accountForUpdate.getEncryptedAesClientKey());
        Assertions.assertEquals("description", accountForUpdate.getDescription());
    }

    @Test
    void toEntityConverterShouldEncryptFields() {
        final PersonalAccount accountForUpdate = new PersonalAccount().toBuilder()
                .uuid(UUID_1).build();

        final PersonalAccountDto dtoForUpdate = new PersonalAccountDto().toBuilder()
                .uuid(UUID_1).build();

        Mockito.when(accountDataService.internalGetById(any())).thenReturn(new AccountData());

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        verify(personalAccountsEncryptor, times(1)).encryptFields(accountForUpdate);
    }

    @Test
    void toEntityConverterShouldSetNewFolder() {
        Mockito.when(accountDataService.internalGetById(any())).thenReturn(new AccountData());

        PersonalAccount accountForUpdate = new PersonalAccount().toBuilder()
                .uuid(UUID_1).build();

        PersonalAccountDto dtoForUpdate = new PersonalAccountDto().toBuilder()
                .uuid(UUID_1).folderUuid(UUID_2).build();

        PersonalAccountFolder folder = PersonalAccountFolder.builder()
                .uuid(UUID_2).build();

        when(folderService.getByUuid(UUID_2)).thenReturn(folder);

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertNotNull(accountForUpdate.getFolder());
        Assertions.assertEquals(UUID_2, accountForUpdate.getFolder().getUuid());

        accountForUpdate = new PersonalAccount().toBuilder().uuid(UUID_1).folder(folder).build();
        dtoForUpdate = new PersonalAccountDto().toBuilder().uuid(UUID_1).folderUuid(null).build();

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertNull(accountForUpdate.getFolder());
    }

    @Test
    void toDtoConverterShouldDecryptFieldsUsingAccountSalt() {
        final PersonalAccount accountForMapping = new PersonalAccount().toBuilder()
                .uuid(UUID_1).duplicatedAccountEntity(new AccountData()).fieldsEncryptionSalt("random").build();

        final PersonalAccountDto resultDto = personalAccountConverter.toDto(accountForMapping);

        verify(personalAccountsEncryptor, times(1)).decryptFields(accountForMapping.getFieldsEncryptionSalt(), resultDto);
    }

    @Test
    void toDtoConverterShouldConvertMainFields() {
        PersonalAccount account = new PersonalAccount().toBuilder()
                .uuid(UUID_1)
                .password("password")
                .username("username")
                .url("url")
                .name("name")
                .description("description")
                .encryptedAesClientKey("encryptedKey")
                .duplicatedAccountEntity(AccountData.builder().publicKey("publicKey").build())
                .folder(PersonalAccountFolder.builder().uuid(UUID_2).build())
                .build();

        PersonalAccountDto dto = personalAccountConverter.toDto(account);

        assertNotNull(dto);

        assertEquals(account.getUuid(), dto.getUuid());
        assertEquals(account.getPassword(), dto.getPassword());
        assertEquals(account.getUsername(), dto.getUsername());
        assertEquals(account.getUrl(), dto.getUrl());
        assertEquals(account.getName(), dto.getName());
        assertEquals(account.getDescription(), dto.getDescription());
        assertEquals(account.getFolder().getUuid(), dto.getFolderUuid());
        assertEquals(account.getDuplicatedAccountEntity().getPublicKey(), dto.getEncryptionPublicKey());
        assertEquals(account.getEncryptedAesClientKey(), "encryptedKey");
    }

}