package com.statkovit.personalAccountsService.unit.services;

import com.querydsl.jpa.impl.JPAQuery;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.repository.expressions.PersonalAccountsQueryBuilder;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountServiceUTest {

    @Mock
    private PersonalAccountRepository personalAccountRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private PersonalAccountsQueryBuilder expressionsBuilder;

    @InjectMocks
    private PersonalAccountService personalAccountService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;

    @Test
    void saveShouldCallRepositoryMethod() {
        final PersonalAccount accountMock = PersonalAccount.builder().uuid(UUID_1).build();

        personalAccountService.save(accountMock);

        verify(personalAccountRepository, times(1)).save(accountMock);
    }

    @Test
    void deleteShouldCallRepositoryDeleteMethod() {
        final PersonalAccount accountMock = PersonalAccount.builder()
                .sharedAccounts(new ArrayList<>())
                .uuid(UUID_1).build();

        personalAccountService.delete(accountMock);

        verify(personalAccountRepository, times(1)).delete(accountMock);
    }

    @Test
    void getListShouldReturnAccountsFromRepository() {
        JPAQuery<PersonalAccount> query = Mockito.mock(JPAQuery.class);
        PersonalAccountListFilters filters = new PersonalAccountListFilters();

        PersonalAccount firstAccount = PersonalAccount.builder()
                .uuid(UUID_1).accountEntityId(ID_1).build();

        PersonalAccount secondAccount = PersonalAccount.builder()
                .uuid(UUID_2).accountEntityId(ID_1).build();

        List<PersonalAccount> allAccounts = List.of(firstAccount, secondAccount);

        when(expressionsBuilder.createListQuery(same(filters))).thenReturn(query);
        when(query.fetch()).thenReturn(allAccounts);

        List<PersonalAccount> result = personalAccountService.getList(filters);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(allAccounts));
    }

    @Test
    void getListCountShouldReturnCountFromRepository() {
        JPAQuery<PersonalAccount> query = Mockito.mock(JPAQuery.class);
        PersonalAccountListFilters filters = new PersonalAccountListFilters();

        when(expressionsBuilder.createListQuery(same(filters))).thenReturn(query);
        when(query.fetchCount()).thenReturn(99L);

        long result = personalAccountService.count(filters);

        assertEquals(99L, result);
    }

    @Test
    void findOneByUuidShouldReturnAccountIfCorrectData() {
        JPAQuery<PersonalAccount> query = Mockito.mock(JPAQuery.class);

        PersonalAccount secondAccount = PersonalAccount.builder()
                .uuid(UUID_2).accountEntityId(ID_2).build();


        when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_2);
        when(expressionsBuilder.createUuidAccountEntityIdQuery(UUID_2, ID_2)).thenReturn(query);
        when(query.fetchOne()).thenReturn(secondAccount);

        final PersonalAccount result = personalAccountService.findOneByUuid(UUID_2);
        assertNotNull(result);
        assertEquals(result.getUuid(), UUID_2);
    }

    @Test
    void findOneByUuidShouldThrowLocalizedExceptionIfIncorrectData() {
        JPAQuery<PersonalAccount> query = Mockito.mock(JPAQuery.class);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(ID_2);
        when(expressionsBuilder.createUuidAccountEntityIdQuery(UUID_1, 2L)).thenReturn(query);

        when(query.fetchOne()).thenReturn(null);

        Exception exception = assertThrows(LocalizedException.class, () ->
                personalAccountService.findOneByUuid(UUID_1)
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }
}