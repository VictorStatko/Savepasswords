package com.statkovit.authorizationservice.utils;

import com.statkovit.authorizationservice.properties.SpringProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfilesManager {
    private final SpringProperties springProperties;

    public boolean isProduction() {
        String activeProfiles = springProperties.getProfiles().getActive();
        return StringUtils.isNotEmpty(activeProfiles) && activeProfiles.contains("prod");
    }
}
