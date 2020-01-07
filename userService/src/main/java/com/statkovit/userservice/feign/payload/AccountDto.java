package com.statkovit.userservice.feign.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private String email;

    private String password;

    private Long id;
}
