package com.statkovit.personalAccountsService.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentAccountEntityId() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        String stringPrincipal = String.valueOf(authentication.getUserAuthentication().getPrincipal());
        return Long.parseLong(stringPrincipal);
    }
}
