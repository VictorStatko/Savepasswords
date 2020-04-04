package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountBuilder;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonalAccountRepositoryITest extends BaseRepositoryTest<PersonalAccount> {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void findAllByAccountEntityIdShouldReturnOnlyRequiredEntities() {
        entityManager.persistAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).build()
        );
        entityManager.persistAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).build()
        );
        entityManager.persistAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(2L).build()
        );

        List<PersonalAccount> result = personalAccountRepository.findAllByAccountEntityId(1L);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.stream().allMatch(account -> Objects.equals(1L, account.getAccountEntityId())));
    }

    @Test
    void findByUuidAndAccountEntityIdShouldReturnOnlyRequiredEntities() {
        entityManager.persistAndFlush(
                prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build()
        );

        entityManager.persistAndFlush(
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