package com.statkovit.authorizationservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class SignInDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
