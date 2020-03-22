package com.statkovit.personalAccountsService.domain;

import com.statkovit.personalAccountsService.domain.base.BaseIndexedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "personal_account")
@SequenceGenerator(name = "default_gen", sequenceName = "personal_account_id_seq", allocationSize = 1)
@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccount extends BaseIndexedEntity {

    public static final int MAX_LENGTH__FIELDS_ENCRYPTION_SALT = 32;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "description")
    private String description;

    @Column(name = "accountEntityId", nullable = false)
    private Long accountEntityId;

    @Column(name = "fields_encryption_salt", nullable = false, length = MAX_LENGTH__FIELDS_ENCRYPTION_SALT)
    private String fieldsEncryptionSalt;
}
