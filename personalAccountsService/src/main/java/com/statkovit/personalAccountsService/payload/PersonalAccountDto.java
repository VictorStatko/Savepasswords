package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalAccountDto {

    @Max(PersonalAccount.MAX_LENGTH__URL)
    private String url;

    @Max(PersonalAccount.MAX_LENGTH__NAME)
    private String name;

    private String username;

    private String password;

    private String description;
}
