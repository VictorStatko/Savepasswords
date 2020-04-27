package com.statkovit.personalAccountsService.repository.expressions;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.QAccountData;
import com.statkovit.personalAccountsService.domain.QPersonalAccount;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonalAccountsQueryBuilder {

    private final SecurityUtils securityUtils;
    private final EntityManager entityManager;

    public JPAQuery<PersonalAccount> createListQuery(PersonalAccountListFilters filters) {
        JPAQueryFactory queryFactory = getFactory();

        final Long accountEntityId = securityUtils.getCurrentAccountEntityId();

        if (Objects.nonNull(filters.getParentPersonalAccountAccountEntityUuid())) {
            QPersonalAccount original = new QPersonalAccount("original");
            QPersonalAccount parent = new QPersonalAccount("parent");
            QAccountData parentAccountData = new QAccountData("parentAccountData");

            return queryFactory.selectFrom(original)
                    .innerJoin(parent).on(original.parentPersonalAccount.id.eq(parent.id))
                    .innerJoin(parentAccountData).on(parent.accountEntityId.eq(parentAccountData.id))
                    .where(original.accountEntityId.eq(accountEntityId)
                            .and(parentAccountData.uuid.eq(filters.getParentPersonalAccountAccountEntityUuid()))
                    );
        }

        BooleanExpression expression = QPersonalAccount.personalAccount
                .accountEntityId.eq(accountEntityId)
                .and(QPersonalAccount.personalAccount.parentPersonalAccount.isNull());

        if (filters.isUnfolderedOnly()) {
            expression = expression.and(
                    QPersonalAccount.personalAccount.folder.isNull()
            );
        } else if (Objects.nonNull(filters.getFolderUuid())) {
            expression = expression.and(
                    QPersonalAccount.personalAccount.folder.uuid.eq(filters.getFolderUuid())
            );
        }

        return queryFactory.selectFrom(QPersonalAccount.personalAccount).where(expression);

    }

    public JPAQuery<PersonalAccount> createUuidAccountEntityIdQuery(UUID uuid, Long accountEntityId) {
        JPAQueryFactory queryFactory = getFactory();

        QPersonalAccount original = new QPersonalAccount("original");
        QPersonalAccount parent = new QPersonalAccount("parent");

        return queryFactory.selectFrom(original)
                .leftJoin(parent).on(original.parentPersonalAccount.id.eq(parent.id))
                .where(original.uuid.eq(uuid)
                        .and(
                                original.accountEntityId.eq(accountEntityId).or(parent.accountEntityId.eq(accountEntityId))
                        )
                );
    }

    public JPAQuery<Tuple> createSharingListQuery() {
        JPAQueryFactory queryFactory = getFactory();

        QPersonalAccount original = new QPersonalAccount("original");
        QPersonalAccount parent = new QPersonalAccount("parent");
        QAccountData parentAccountData = new QAccountData("parentAccountData");

        return queryFactory.from(original)
                .innerJoin(parent).on(original.parentPersonalAccount.id.eq(parent.id))
                .innerJoin(parentAccountData).on(parent.accountEntityId.eq(parentAccountData.id))
                .where(original.accountEntityId.eq(securityUtils.getCurrentAccountEntityId()))
                .groupBy(parentAccountData.id)
                .select(parentAccountData, parentAccountData.count());
    }

    private JPAQueryFactory getFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
