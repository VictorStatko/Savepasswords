package com.statkovit.personalAccountsService.integration.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.lang.reflect.ParameterizedType;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@EnableJpaAuditing
@ActiveProfiles("integration-test")
public abstract class BaseRepositoryTest<T> {

    @MockBean
    public ResourceServerProperties resourceServerProperties;

    @Autowired
    protected TestEntityManager entityManager;

    private final Class<T> type = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];

    protected int deleteAllEntities() {
        CriteriaBuilder builder = entityManager.getEntityManager().getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(type);
        query.from(type);
        return entityManager.getEntityManager().createQuery(query).executeUpdate();
    }
}
