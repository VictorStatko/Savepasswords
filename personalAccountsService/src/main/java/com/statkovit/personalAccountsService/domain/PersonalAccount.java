package com.statkovit.personalAccountsService.domain;

import com.statkovit.personalAccountsService.domain.base.BaseIndexedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "personal_account")
@SequenceGenerator(name = "default_gen", sequenceName = "personal_account_id_seq", allocationSize = 1)
@Getter
@Setter
public class PersonalAccount extends BaseIndexedEntity {

    public static final int MAX_LENGTH__URL = 2047;
    public static final int MAX_LENGTH__NAME = 254;
    public static final int MAX_LENGTH__USERNAME = 254;

    @Column(name = "url", length = MAX_LENGTH__URL)
    private String url;

    @Column(name = "name", length = MAX_LENGTH__NAME)
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "description")
    private String description;

    @Column(name = "accountEntityId", nullable = false)
    private Long accountEntityId;
}
