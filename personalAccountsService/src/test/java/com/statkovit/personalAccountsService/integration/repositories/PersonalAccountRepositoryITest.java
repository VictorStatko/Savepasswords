package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountBuilder;


class PersonalAccountRepositoryITest extends BaseRepositoryTest {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void save_fieldsEncryptionSaltCanNotBeNull() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                personalAccountRepository.saveAndFlush(
                        prePopulatedValidAccountBuilder().fieldsEncryptionSalt(null).build()
                )
        );
    }

    @Test
    void save_fieldsEncryptionUrlOrNameCanNotBeNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                personalAccountRepository.saveAndFlush(
                        prePopulatedValidAccountBuilder().url(null).name(null).build()
                )
        );

        Assertions.assertDoesNotThrow(() -> {
            personalAccountRepository.saveAndFlush(
                    prePopulatedValidAccountBuilder().url("url").build()
            );
        });

        Assertions.assertDoesNotThrow(() -> {
            personalAccountRepository.saveAndFlush(
                    prePopulatedValidAccountBuilder().name("name").build()
            );
        });

        Assertions.assertDoesNotThrow(() -> {
            personalAccountRepository.saveAndFlush(
                    prePopulatedValidAccountBuilder().url("url").name("name").build()
            );
        });
    }

    @Test
    void findAllByAccountEntityIdShouldReturnOnlyRequiredEntities() {
        personalAccountRepository.saveAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).build()
        );
        personalAccountRepository.saveAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).build()
        );
        personalAccountRepository.saveAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(2L).build()
        );

        List<PersonalAccount> result = personalAccountRepository.findAllByAccountEntityId(1L);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.stream().allMatch(account -> Objects.equals(1L, account.getAccountEntityId())));
    }

    @Test
    void findByUuidAndAccountEntityIdShouldReturnOnlyRequiredEntities() {
        personalAccountRepository.saveAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build()
        );

        personalAccountRepository.saveAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).build()
        );

        Optional<PersonalAccount> optional = personalAccountRepository.findByUuidAndAccountEntityId(UUID_1, 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals(UUID_1, optional.get().getUuid());

        optional = personalAccountRepository.findByUuidAndAccountEntityId(UUID_2, 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals(UUID_2, optional.get().getUuid());

        optional = personalAccountRepository.findByUuidAndAccountEntityId(UUID_1, 2L);

        Assertions.assertTrue(optional.isEmpty());
    }
}