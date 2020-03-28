package com.statkovit.personalAccountsService.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public Long getCurrentAccountEntityId() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        String stringPrincipal = String.valueOf(authentication.getUserAuthentication().getPrincipal());
        return Long.parseLong(stringPrincipal);
    }
}
