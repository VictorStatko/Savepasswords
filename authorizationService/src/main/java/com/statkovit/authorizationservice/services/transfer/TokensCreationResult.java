package com.statkovit.authorizationservice.services.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokensCreationResult {
    private String accessToken;
    private String refreshToken;
    private String accessTokenExpiration;
}
