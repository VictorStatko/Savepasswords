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
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonalAccountServiceTest {

    @Mock
    private PersonalAccountRepository personalAccountRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PersonalAccountServiceImpl personalAccountService;

    @Test
    void saveShouldReturnEntityWithIdAndUuid() {
        final Long acccountEntityId = getRandomLong();
        final Long acccountId = getRandomLong();
        final UUID accountUUID = UUID.randomUUID();

        final PersonalAccount accountMock = createAccountMock(acccountId, accountUUID, acccountEntityId);

        when(personalAccountRepository.save(accountMock)).thenReturn(accountMock);

        personalAccountService.save(accountMock);

        verify(personalAccountRepository, times(1)).save(accountMock);
    }

    @Test
    void deleteShouldCallRepositoryMethodExactlyOnce() {
        final Long acccountEntityId = getRandomLong();
        final UUID accountUUID = UUID.randomUUID();

        final PersonalAccount accountMock = createAccountMock(null, accountUUID, acccountEntityId);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(acccountEntityId);

        when(personalAccountRepository.findByUuidAndAccountEntityId(accountUUID, acccountEntityId))
                .thenReturn(Optional.of(accountMock));

        personalAccountService.delete(accountUUID);

        verify(personalAccountRepository, times(1)).delete(accountMock);
    }

    @Test
    void getListShouldReturnAccountsOfCurrentEntityId() {
        final UUID firstAccountUUID = UUID.randomUUID();
        final Long firstAccountEntityId = getRandomLong();
        final UUID secondAccountUUID = UUID.randomUUID();
        final Long secondAccountEntityId = getRandomLong();
        final Long thirdAccountEntityId = getRandomLong();

        PersonalAccount firstAccount = createAccountMock(null, firstAccountUUID, firstAccountEntityId);
        PersonalAccount secondAccount = createAccountMock(null, secondAccountUUID, secondAccountEntityId);

        List<PersonalAccount> allAccounts = List.of(firstAccount, secondAccount);

        when(personalAccountRepository.findAllByAccountEntityId(anyLong()))
                .then(invocation -> {
                    Long accountEntityId = invocation.getArgument(0);
                    return allAccounts.stream().filter(account -> accountEntityId.equals(account.getAccountEntityId())).collect(Collectors.toList());
                });

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(secondAccountEntityId);

        List<PersonalAccount> result = personalAccountService.getList();
        assertEquals(1, result.size());
        assertEquals(secondAccountUUID, result.get(0).getUuid());

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(firstAccountEntityId);

        result = personalAccountService.getList();
        assertEquals(1, result.size());
        assertEquals(firstAccountUUID, result.get(0).getUuid());

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(thirdAccountEntityId);

        result = personalAccountService.getList();
        assertEquals(0, result.size());
    }

    @Test
    void findOneByUuidShouldReturnAccountIfCorrectData() {
        final UUID firstAccountUUID = UUID.randomUUID();
        final Long firstAccountEntityId = getRandomLong();
        final UUID secondAccountUUID = UUID.randomUUID();
        final Long secondAccountEntityId = getRandomLong();

        PersonalAccount firstAccount = createAccountMock(null, firstAccountUUID, firstAccountEntityId);
        PersonalAccount secondAccount = createAccountMock(null, secondAccountUUID, secondAccountEntityId);

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
        final UUID firstAccountUUID = UUID.randomUUID();
        final Long firstAccountEntityId = getRandomLong();
        final UUID secondAccountUUID = UUID.randomUUID();
        final Long secondAccountEntityId = getRandomLong();

        PersonalAccount firstAccount = createAccountMock(null, firstAccountUUID, firstAccountEntityId);
        PersonalAccount secondAccount = createAccountMock(null, secondAccountUUID, secondAccountEntityId);

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

    private PersonalAccount createAccountMock(Long id, UUID uuid, Long entityId) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setId(id);
        accountMock.setUuid(uuid);
        accountMock.setAccountEntityId(entityId);
        return accountMock;
    }

    private Long getRandomLong() {
        Random random = new Random();
        return random.nextLong();
    }
}