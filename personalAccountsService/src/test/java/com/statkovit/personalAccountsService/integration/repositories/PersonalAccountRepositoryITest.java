package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.repository.AccountDataRepository;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.AccountDataDomainHelper.prePopulatedValidAccountDataBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountBuilder;


class PersonalAccountRepositoryITest extends BaseRepositoryTest {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    @Autowired
    AccountDataRepository accountDataRepository;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @BeforeEach
    void beforeEach() {
        accountDataRepository.saveAndFlush(
                prePopulatedValidAccountDataBuilder()
                        .id(1L)
                        .email("email1@test.com")
                        .uuid(UUID_1)
                        .build()
        );

        accountDataRepository.saveAndFlush(
                prePopulatedValidAccountDataBuilder()
                        .id(2L)
                        .email("email2@test.com")
                        .uuid(UUID_2)
                        .build()
        );
    }

    @Test
    void save_fieldsEncryptionSaltCanNotBeNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
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
}