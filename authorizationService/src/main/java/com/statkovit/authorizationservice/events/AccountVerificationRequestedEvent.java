package com.statkovit.authorizationservice.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class AccountVerificationRequestedEvent {
    private String locale;
    private String email;
    private UUID uuid;
    private String verificationCode;
    private int expirationOnHours;
}
