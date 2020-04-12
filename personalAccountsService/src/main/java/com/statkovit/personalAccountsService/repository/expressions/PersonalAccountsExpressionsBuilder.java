package com.statkovit.personalAccountsService.repository.expressions;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.statkovit.personalAccountsService.domain.QPersonalAccount;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonalAccountsExpressionsBuilder {

    private final SecurityUtils securityUtils;

    public BooleanExpression getListExpression(PersonalAccountListFilters filters) {
        BooleanExpression expression = QPersonalAccount.personalAccount
                .accountEntityId.eq(securityUtils.getCurrentAccountEntityId());

        if (filters.isUnfolderedOnly()) {
            expression = expression.and(
                    QPersonalAccount.personalAccount.folder.isNull()
            );
        } else if (Objects.nonNull(filters.getFolderUuid())) {
            expression = expression.and(
                    QPersonalAccount.personalAccount.folder.uuid.eq(UUID.fromString(filters.getFolderUuid()))
            );
        }


        return expression;

    }

    ;
}
