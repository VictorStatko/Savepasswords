package com.statkovit.userservice.dto;

import com.statkovit.userservice.domain.Account;
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
public class SignUpDTO extends CredentialsDTO {
    @NotBlank
    @Size(max = Account.MAX_LENGTH__NAME)
    private String name;
}
