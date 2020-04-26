package com.statkovit.personalAccountsService.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "account_data")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AccountData {

    @Id
    @Column(name = "id")
    private Long id;

    @Type(type = "pg-uuid")
    @NotNull
    @Column(name = "uuid", updatable = false)
    private UUID uuid;

    @NotNull
    @Column(name = "version")
    public Long version;

    @NotEmpty
    @Column(name = "email")
    public String email;

    @NotEmpty
    @Column(name = "public_key")
    public String publicKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountData that = (AccountData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }

}
