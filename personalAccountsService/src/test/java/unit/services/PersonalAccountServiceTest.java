package unit.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.services.impl.PersonalAccountServiceImpl;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnnecessaryLocalVariable")
@ExtendWith(MockitoExtension.class)
public class PersonalAccountServiceTest {

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
    void saveShouldReturnEntityWithIdAndUuid() {
        final PersonalAccount accountMock = account();

        personalAccountService.save(accountMock);

        verify(personalAccountRepository, times(1)).save(accountMock);
    }

    @Test
    void deleteShouldCallRepositoryMethodExactlyOnce() {
        final Long acccountEntityId = ID_1;
        final UUID accountUUID = UUID_1;

        final PersonalAccount accountMock = account(accountUUID, acccountEntityId);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(acccountEntityId);

        when(personalAccountRepository.findByUuidAndAccountEntityId(accountUUID, acccountEntityId))
                .thenReturn(Optional.of(accountMock));

        personalAccountService.delete(accountUUID);

        verify(personalAccountRepository, times(1)).delete(accountMock);
    }

    @Test
    void getListShouldReturnAccountsOfCurrentEntityId() {
        final Long firstAccountEntityId = ID_1;
        final Long secondAccountEntityId = ID_2;
        final Long thirdAccountEntityId = ID_3;

        PersonalAccount firstAccount = account(firstAccountEntityId);
        PersonalAccount secondAccount = account(secondAccountEntityId);

        List<PersonalAccount> allAccounts = List.of(firstAccount, secondAccount);

        when(personalAccountRepository.findAllByAccountEntityId(anyLong()))
                .then(invocation -> {
                    Long accountEntityId = invocation.getArgument(0);
                    return allAccounts.stream().filter(account -> accountEntityId.equals(account.getAccountEntityId())).collect(Collectors.toList());
                });

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(secondAccountEntityId);

        List<PersonalAccount> result = personalAccountService.getList();
        assertEquals(1, result.size());
        assertSame(secondAccount, result.get(0));

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(firstAccountEntityId);

        result = personalAccountService.getList();
        assertEquals(1, result.size());
        assertSame(firstAccount, result.get(0));

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(thirdAccountEntityId);

        result = personalAccountService.getList();
        assertEquals(0, result.size());
    }

    @Test
    void findOneByUuidShouldReturnAccountIfCorrectData() {
        final UUID firstAccountUUID = UUID_1;
        final Long firstAccountEntityId = ID_1;
        final UUID secondAccountUUID = UUID_2;
        final Long secondAccountEntityId = ID_2;

        PersonalAccount firstAccount = account(firstAccountUUID, firstAccountEntityId);
        PersonalAccount secondAccount = account(secondAccountUUID, secondAccountEntityId);

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(secondAccountEntityId);

        when(personalAccountRepository.findByUuidAndAccountEntityId(any(UUID.class), anyLong()))
                .then(invocation -> {
                    UUID uuid = invocation.getArgument(0);
                    Long accountEntityId = invocation.getArgument(1);
                    return accounts.stream().filter(account ->
                            uuid.equals(account.getUuid())
                                    && accountEntityId.equals(account.getAccountEntityId())
                    ).findFirst();
                });

        personalAccountService.findOneByUuid(secondAccountUUID);
    }

    @Test
    void findOneByUuidShouldThrowLocalizedExceptionIfIncorrectData() {
        final UUID firstAccountUUID = UUID_1;
        final Long firstAccountEntityId = ID_1;
        final UUID secondAccountUUID = UUID_2;
        final Long secondAccountEntityId = ID_2;

        PersonalAccount firstAccount = account(firstAccountUUID, firstAccountEntityId);
        PersonalAccount secondAccount = account(secondAccountUUID, secondAccountEntityId);

        List<PersonalAccount> accounts = List.of(firstAccount, secondAccount);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(secondAccountEntityId);

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
                personalAccountService.findOneByUuid(firstAccountUUID)
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }


    private PersonalAccount account() {
        return new PersonalAccount();
    }

    private PersonalAccount account(UUID uuid, Long entityId) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setUuid(uuid);
        accountMock.setAccountEntityId(entityId);
        return accountMock;
    }

    private PersonalAccount account(Long entityId) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setAccountEntityId(entityId);
        return accountMock;
    }
}