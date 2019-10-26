package com.statkovit.userservice.dto;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDTO {

    @Email
    @NotBlank
    @Size(max = Account.MAX_LENGTH__EMAIL)
    private String email;

    @NotBlank
    @Size(max = Account.MAX_LENGTH__PASSWORD)
    private String password;

}
