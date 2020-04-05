package com.statkovit.personalAccountsService.domain;

import com.statkovit.personalAccountsService.domain.base.BaseAccountEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_account_folder")
@SequenceGenerator(name = "default_gen", sequenceName = "personal_account_folder_id_seq", allocationSize = 1)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PersonalAccountFolder extends BaseAccountEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "folder")
    private List<PersonalAccount> accounts = new ArrayList<>();

}
