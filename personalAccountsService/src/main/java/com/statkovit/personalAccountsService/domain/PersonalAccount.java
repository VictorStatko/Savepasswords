package com.statkovit.personalAccountsService.domain;

import com.statkovit.personalAccountsService.domain.base.BaseAccountEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_account")
@SequenceGenerator(name = "default_gen", sequenceName = "personal_account_id_seq", allocationSize = 1)
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccount extends BaseAccountEntity {

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

    @NotNull
    @Size(max = MAX_LENGTH__FIELDS_ENCRYPTION_SALT)
    @Column(name = "fields_encryption_salt")
    private String fieldsEncryptionSalt;

    @NotNull
    @Column(name = "encrypted_aes_client_key")
    private String encryptedAesClientKey;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private PersonalAccountFolder folder;

    @ManyToOne
    @JoinColumn(name = "parent_personal_account_id")
    private PersonalAccount parentPersonalAccount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPersonalAccount", orphanRemoval = true)
    private List<PersonalAccount> sharedAccounts = new ArrayList<>();
}
