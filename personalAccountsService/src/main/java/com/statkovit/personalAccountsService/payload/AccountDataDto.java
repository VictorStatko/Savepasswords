package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@NoArgsConstructor
public class AccountDataDto {

    public UUID uuid;
    public Long id;
    public Long version;
    public String email;
    public String publicKey;
}
