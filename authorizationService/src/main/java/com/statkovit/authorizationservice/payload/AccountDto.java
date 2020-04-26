package com.statkovit.authorizationservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statkovit.authorizationservice.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountDto {

    private UUID uuid;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    private String clientPasswordSalt;

    @NotEmpty
    private String privateKey;

    @NotEmpty
    private String publicKey;
}
