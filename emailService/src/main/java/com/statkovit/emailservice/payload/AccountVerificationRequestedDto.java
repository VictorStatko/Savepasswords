package com.statkovit.emailservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AccountVerificationRequestedDto {
    private String locale;
    private String email;
    private UUID uuid;
    private String verificationCode;
    private int expirationOnHours;
}
