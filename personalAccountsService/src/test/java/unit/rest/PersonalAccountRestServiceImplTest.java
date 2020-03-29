package unit.rest;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static unit.helper.domain.PersonalAccountDomainHelper.account;
import static unit.helper.domain.PersonalAccountDomainHelper.accountDTO;

@ExtendWith(MockitoExtension.class)
class PersonalAccountRestServiceImplTest {

    @Mock
    private PersonalAccountService personalAccountService;

    @Mock
    private PersonalAccountMapper personalAccountMapper;

    @InjectMocks
    private PersonalAccountRestServiceImpl personalAccountRestServiceImpl;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void createShouldReturnDtoOfSavedAccount() {
        final PersonalAccountDto dtoBeforeSave = accountDTO();
        final PersonalAccount accountBeforeSave = account();

        final PersonalAccountDto dtoAfterSave = accountDTO();
        final PersonalAccount accountAfterSave = account();

        when(personalAccountMapper.toEntity(same(dtoBeforeSave), any(PersonalAccount.class)))
                .thenReturn(accountBeforeSave);
        when(personalAccountService.save(accountBeforeSave)).thenReturn(accountAfterSave);
        when(personalAccountMapper.toDto(accountAfterSave)).thenReturn(dtoAfterSave);

        PersonalAccountDto resultDto = personalAccountRestServiceImpl.create(dtoBeforeSave);

        Assertions.assertSame(dtoAfterSave, resultDto);
    }

    @Test
    void updateShouldReturnDtoOfSavedAccount() {
        final UUID accountUuid = UUID_1;
        final PersonalAccountDto dtoBeforeUpdate = accountDTO();
        final PersonalAccount accountForUpdate = account(accountUuid);

        final PersonalAccountDto dtoAfterUpdate = accountDTO(accountUuid);
        final PersonalAccount accountAfterUpdate = account(accountUuid);

        when(personalAccountService.findOneByUuid(accountUuid)).thenReturn(accountForUpdate);
        when(personalAccountService.save(accountAfterUpdate)).thenReturn(accountAfterUpdate);
        when(personalAccountMapper.toEntity(dtoBeforeUpdate, accountForUpdate)).thenReturn(accountAfterUpdate);
        when(personalAccountMapper.toDto(accountAfterUpdate)).thenReturn(dtoAfterUpdate);

        final PersonalAccountDto resultDto = personalAccountRestServiceImpl.update(accountUuid, dtoBeforeUpdate);

        Assertions.assertSame(resultDto, dtoAfterUpdate);

    }

    @Test
    void getListShouldReturnListOfDtos() {
        PersonalAccount firstAccount = account();
        PersonalAccountDto firstAccountDto = accountDTO();
        PersonalAccount secondAccount = account();
        PersonalAccountDto secondAccountDto = accountDTO();

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(personalAccountService.getList()).thenReturn(accounts);

        when(personalAccountMapper.toDto(any(PersonalAccount.class))).thenAnswer(invocation -> {
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
        Assertions.assertSame(resultDtos.get(0), firstAccountDto);
        Assertions.assertSame(resultDtos.get(1), secondAccountDto);
    }

    @Test
    void deleteShouldCallServiceMethodExactlyOnce() {
        final UUID accountUUID = UUID_1;

        personalAccountRestServiceImpl.delete(accountUUID);

        verify(personalAccountService, times(1)).delete(accountUUID);
    }
}