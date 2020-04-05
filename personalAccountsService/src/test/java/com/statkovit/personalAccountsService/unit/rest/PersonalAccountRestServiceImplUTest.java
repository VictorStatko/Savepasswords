package com.statkovit.personalAccountsService.unit.rest;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountConverter;
import com.statkovit.personalAccountsService.rest.impl.PersonalAccountRestServiceImpl;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.accountDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountRestServiceImplUTest {

    @Mock
    private PersonalAccountService personalAccountService;

    @Mock
    private PersonalAccountConverter personalAccountConverter;

    @InjectMocks
    private PersonalAccountRestServiceImpl personalAccountRestServiceImpl;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void createShouldReturnDtoOfSavedAccount() {
        final PersonalAccountDto dtoBeforeSave = accountDto();

        final PersonalAccountDto dtoAfterSave = PersonalAccountDto.builder().uuid(UUID_1).build();
        final PersonalAccount accountAfterSave = PersonalAccount.builder().uuid(UUID_1).build();

        when(personalAccountService.save(any(PersonalAccount.class))).thenReturn(accountAfterSave);
        when(personalAccountConverter.toDto(accountAfterSave)).thenReturn(dtoAfterSave);

        PersonalAccountDto resultDto = personalAccountRestServiceImpl.create(dtoBeforeSave);

        Assertions.assertEquals(dtoAfterSave, resultDto);
    }


    @Test
    void createShouldCallConverterToEntity() {
        final PersonalAccountDto dto = accountDto();

        personalAccountRestServiceImpl.create(dto);

        verify(personalAccountConverter, times(1)).toEntity(same(dto), any(PersonalAccount.class));
    }

    @Test
    void updateShouldReturnDtoOfSavedAccount() {
        final PersonalAccountDto dtoBeforeUpdate = PersonalAccountDto.builder().uuid(UUID_1).build();
        final PersonalAccount accountForUpdate = PersonalAccount.builder().uuid(UUID_1).build();

        final PersonalAccountDto dtoAfterUpdate = PersonalAccountDto.builder().uuid(UUID_1).build();
        final PersonalAccount accountAfterUpdate = PersonalAccount.builder().uuid(UUID_1).build();

        when(personalAccountService.findOneByUuid(UUID_1)).thenReturn(accountForUpdate);
        when(personalAccountService.save(accountForUpdate)).thenReturn(accountAfterUpdate);
        when(personalAccountConverter.toDto(accountAfterUpdate)).thenReturn(dtoAfterUpdate);

        final PersonalAccountDto resultDto = personalAccountRestServiceImpl.update(UUID_1, dtoBeforeUpdate);

        Assertions.assertEquals(resultDto, dtoAfterUpdate);
    }

    @Test
    void updateShouldCallConverterToEntity() {
        final PersonalAccountDto dtoForUpdate = PersonalAccountDto.builder().uuid(UUID_1).build();
        final PersonalAccount accountForUpdate = PersonalAccount.builder().uuid(UUID_1).build();

        when(personalAccountService.findOneByUuid(UUID_1)).thenReturn(accountForUpdate);

        personalAccountRestServiceImpl.update(UUID_1, dtoForUpdate);

        verify(personalAccountConverter, times(1)).toEntity(dtoForUpdate, accountForUpdate);
    }

    @Test
    void getListShouldReturnListOfDtos() {
        PersonalAccount firstAccount = PersonalAccount.builder().uuid(UUID_1).build();
        PersonalAccountDto firstAccountDto = PersonalAccountDto.builder().uuid(UUID_1).build();
        PersonalAccount secondAccount = PersonalAccount.builder().uuid(UUID_1).build();
        PersonalAccountDto secondAccountDto = PersonalAccountDto.builder().uuid(UUID_1).build();

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(personalAccountService.getList()).thenReturn(accounts);

        when(personalAccountConverter.toDto(any(PersonalAccount.class))).thenAnswer(invocation -> {
            PersonalAccount param = invocation.getArgument(0);
            if (param == firstAccount) {
                return firstAccountDto;
            }
            if (param == secondAccount) {
                return secondAccountDto;
            }
            throw new RuntimeException("Invalid account");
        });

        List<PersonalAccountDto> resultDtos = personalAccountRestServiceImpl.getList();

        Assertions.assertEquals(resultDtos.size(), 2);
        Assertions.assertEquals(resultDtos.get(0), firstAccountDto);
        Assertions.assertEquals(resultDtos.get(1), secondAccountDto);
    }

    @Test
    void deleteShouldCallServiceMethodExactlyOnce() {
        personalAccountRestServiceImpl.delete(UUID_1);

        verify(personalAccountService, times(1)).delete(UUID_1);
    }
}