package com.statkovit.authorizationservice.domain;

import com.statkovit.authorizationservice.domain.base.BaseIndexedEntity;
import com.statkovit.authorizationservice.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "account")
@SequenceGenerator(name = "default_gen", sequenceName = "account_id_seq", allocationSize = 1)
@Getter
@Setter
public class Account extends BaseIndexedEntity {

    public static final int MAX_LENGTH__EMAIL = 254;
    public static final int MAX_LENGTH__PASSWORD = 60;
    public static final int MAX_LENGTH__NAME = 254;

    @Column(name = "email", nullable = false, unique = true, length = MAX_LENGTH__EMAIL)
    @Email
    private String email;

    @Column(name = "password", nullable = false, length = MAX_LENGTH__PASSWORD)
    private String password;

    @Column(name = "name", nullable = false, length = MAX_LENGTH__NAME)
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
