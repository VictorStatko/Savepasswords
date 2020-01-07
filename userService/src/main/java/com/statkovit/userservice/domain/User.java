package com.statkovit.userservice.domain;

import com.statkovit.userservice.domain.base.BaseIndexedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@SequenceGenerator(name = "default_gen", sequenceName = "users_id_seq", allocationSize = 1)
@Getter
@Setter
public class User extends BaseIndexedEntity {
    public static final int MAX_LENGTH__NAME = 254;

    @Column(name = "name", nullable = false, length = MAX_LENGTH__NAME)
    private String name;

    @Column(name = "account_id")
    private Long accountId;
}
