package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

class PersonalAccountRepositoryITest extends BaseRepositoryTestsConfig {

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByAccountEntityId() {

    }

    @Test
    void findByUuidAndAccountEntityId() {
    }
}