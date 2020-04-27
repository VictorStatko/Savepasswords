package com.statkovit.personalAccountsService.domain.base;

import com.statkovit.personalAccountsService.domain.AccountData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseAccountEntity extends BaseIndexedEntity {

    @NotNull
    @Column(name = "account_entity_id", nullable = false)
    private Long accountEntityId;

    @ManyToOne
    @JoinColumn(name = "account_entity_id", nullable = false, insertable = false, updatable = false)
    private AccountData duplicatedAccountEntity;
}
