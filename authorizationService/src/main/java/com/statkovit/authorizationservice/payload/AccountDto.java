package com.statkovit.authorizationservice.payload;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountDto {

    private Long id;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = Account.MIN_LENGTH__PASSWORD, max = Account.MAX_LENGTH__PASSWORD)
    private String password;
}
