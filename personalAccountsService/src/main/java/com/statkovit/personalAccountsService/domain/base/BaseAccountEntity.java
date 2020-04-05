package com.statkovit.personalAccountsService.domain.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseAccountEntity extends BaseIndexedEntity {

    @Column(name = "account_entity_id", nullable = false)
    private Long accountEntityId;

}
