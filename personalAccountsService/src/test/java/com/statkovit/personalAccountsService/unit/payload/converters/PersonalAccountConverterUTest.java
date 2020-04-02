package com.statkovit.personalAccountsService.unit.payload.converters;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountConverter;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.account;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.accountDTO;

@ExtendWith(MockitoExtension.class)
class PersonalAccountConverterUTest {

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final Long ID_1 = 1L;

    @Mock
    private PersonalAccountMapper personalAccountMapper;

    @Mock
    private PersonalAccountsEncryptor personalAccountsEncryptor;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PersonalAccountConverter personalAccountConverter;

    @Test
    void toEntityConverterShouldSetAccountEntityIdForNewAccount() {
        final PersonalAccount accountForUpdate = account();
        final PersonalAccountDto dtoForUpdate = accountDTO();

        Mockito.when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_1);

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertEquals(ID_1, accountForUpdate.getAccountEntityId());
    }

    @Test
    void toEntityConverterShouldNotUpdateAccountEntityIdForOldAccount() {
        final PersonalAccount accountForUpdate = account(UUID_1, ID_1);
        final PersonalAccountDto dtoForUpdate = accountDTO(UUID_1);

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        Assertions.assertEquals(ID_1, accountForUpdate.getAccountEntityId());
    }

    @Test
    void toEntityConverterShouldCallEntityMapper() {
        final PersonalAccount accountForUpdate = account(UUID_1);
        final PersonalAccountDto dtoForUpdate = accountDTO(UUID_1);

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        verify(personalAccountMapper, times(1)).toEntity(dtoForUpdate, accountForUpdate);
    }


    @Test
    void toEntityConverterShouldEncryptFields() {
        final PersonalAccount accountForUpdate = account(UUID_1);
        final PersonalAccountDto dtoForUpdate = accountDTO(UUID_1);

        personalAccountConverter.toEntity(dtoForUpdate, accountForUpdate);

        verify(personalAccountsEncryptor, times(1)).encryptFields(accountForUpdate);
    }

    @Test
    void toDtoConverterShouldReturnEntityFromEntityMapper() {
        final PersonalAccount accountForMapping = account(UUID_1);
        final PersonalAccountDto dtoAfterMapping = accountDTO(UUID_1);

        Mockito.when(personalAccountMapper.toDto(accountForMapping)).thenReturn(dtoAfterMapping);

        final PersonalAccountDto resultDto = personalAccountConverter.toDto(accountForMapping);

        Assertions.assertEquals(resultDto, dtoAfterMapping);
    }

    @Test
    void toDtoConverterShouldDecryptFieldsUsingAccountSalt() {
        final PersonalAccount accountForMapping = account(UUID_1);
        accountForMapping.setFieldsEncryptionSalt("random");

        final PersonalAccountDto dtoAfterMapping = accountDTO(UUID_1);

        Mockito.when(personalAccountMapper.toDto(accountForMapping)).thenReturn(dtoAfterMapping);

        final PersonalAccountDto resultDto = personalAccountConverter.toDto(accountForMapping);

        verify(personalAccountsEncryptor, times(1)).decryptFields(accountForMapping.getFieldsEncryptionSalt(), resultDto);
    }

}