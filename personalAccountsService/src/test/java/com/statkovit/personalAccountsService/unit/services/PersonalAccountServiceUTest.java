package com.statkovit.personalAccountsService.unit.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.services.impl.PersonalAccountServiceImpl;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountServiceUTest {

    @Mock
    private PersonalAccountRepository personalAccountRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PersonalAccountServiceImpl personalAccountService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final Long ID_3 = 3L;

    @Test
    void saveShouldCallRepositoryMethod() {
        final PersonalAccount accountMock = PersonalAccount.builder().uuid(UUID_1).build();

        personalAccountService.save(accountMock);

        verify(personalAccountRepository, times(1)).save(accountMock);
    }

    @Test
    void deleteShouldCallRepositoryDeleteMethod() {
        final PersonalAccount accountMock = PersonalAccount.builder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_1);

        when(personalAccountRepository.findByUuidAndAccountEntityId(UUID_1, ID_1))
                .thenReturn(Optional.of(accountMock));

        personalAccountService.delete(UUID_1);

        verify(personalAccountRepository, times(1)).delete(accountMock);
    }

    @Test
    void getListShouldReturnAccountsFromRepository() {
        PersonalAccount firstAccount = PersonalAccount.builder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        PersonalAccount secondAccount = PersonalAccount.builder()
                .uuid(UUID_2).accountEntityId(ID_1).build();

        BooleanExpression expression = Mockito.mock(BooleanExpression.class);

        List<PersonalAccount> allAccounts = List.of(firstAccount, secondAccount);

        when(personalAccountRepository.findAll(expression)).thenReturn(allAccounts);


        List<PersonalAccount> result = personalAccountService.getList(expression);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(allAccounts));
    }

    @Test
    void findOneByUuidShouldReturnAccountIfCorrectData() {
        PersonalAccount firstAccount = PersonalAccount.builder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        PersonalAccount secondAccount = PersonalAccount.builder()
                .uuid(UUID_2).accountEntityId(ID_2).build();

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_2);

        when(personalAccountRepository.findByUuidAndAccountEntityId(any(UUID.class), anyLong()))
                .then(invocation -> {
                    UUID uuid = invocation.getArgument(0);
                    Long accountEntityId = invocation.getArgument(1);
                    return accounts.stream().filter(account ->
                            uuid.equals(account.getUuid())
                                    && accountEntityId.equals(account.getAccountEntityId())
                    ).findFirst();
                });

        final PersonalAccount result = personalAccountService.findOneByUuid(UUID_2);
        assertNotNull(result);
        assertEquals(result.getUuid(), UUID_2);
    }

    @Test
    void findOneByUuidShouldThrowLocalizedExceptionIfIncorrectData() {
        PersonalAccount firstAccount = PersonalAccount.builder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        PersonalAccount secondAccount = PersonalAccount.builder()
                .uuid(UUID_2).accountEntityId(ID_2).build();

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_2);

        when(personalAccountRepository.findByUuidAndAccountEntityId(any(UUID.class), anyLong()))
                .then(invocation -> {
                    UUID uuid = invocation.getArgument(0);
                    Long accountEntityId = invocation.getArgument(1);
                    return accounts.stream().filter(account ->
                            uuid.equals(account.getUuid())
                                    && accountEntityId.equals(account.getAccountEntityId())
                    ).findFirst();
                });

        Exception exception = assertThrows(LocalizedException.class, () ->
                personalAccountService.findOneByUuid(UUID_1)
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }
}