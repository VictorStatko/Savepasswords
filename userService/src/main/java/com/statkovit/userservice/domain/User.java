package com.statkovit.userservice.domain;

import com.statkovit.userservice.domain.base.BaseIndexedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@SequenceGenerator(name = "default_gen", sequenceName = "user_id_seq", allocationSize = 1)
public class User extends BaseIndexedEntity {

    private static final int MAX_LENGTH__EMAIL = 254;
    private static final int MAX_LENGTH__PASSWORD = 60;

    @Column(name = "email", nullable = false, unique = true, length = MAX_LENGTH__EMAIL)
    private String email;

    @Column(name = "password", nullable = false, length = MAX_LENGTH__PASSWORD)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
