package com.statkovit.authorizationservice.domainServices;

import com.statkovit.authorizationservice.configuration.enhancers.AdditionalInformationTokenEnhancer;
import com.statkovit.authorizationservice.payload.SessionDto;
import com.statkovit.authorizationservice.services.AuthSessionService;
import com.statkovit.authorizationservice.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AuthSessionDomainService {

    private final AuthSessionService authSessionService;

    public void logout() {
        authSessionService.logout();
    }

    public List<SessionDto> getSessionsList() {
        Optional<String> currentBearerToken = WebUtils.getBearerTokenValue();
        List<OAuth2AccessToken> accessTokens = authSessionService.getSessionsList();
        List<SessionDto> dtos = new ArrayList<>();

        accessTokens.forEach(oAuth2AccessToken -> {
            SessionDto dto = new SessionDto();
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            dto.setIpAddress((String) additionalInformation.get(AdditionalInformationTokenEnhancer.CLIENT_IP_ADDRESS));
            dto.setCreatedAt((Instant) additionalInformation.get(AdditionalInformationTokenEnhancer.CREATED_AT));
            dto.setCurrent(currentBearerToken.map(token -> token.equals(oAuth2AccessToken.getValue())).orElse(false));
            dto.setGeoInfo((String) additionalInformation.get(AdditionalInformationTokenEnhancer.GEO_INFO));
            dto.setSystemInfo((String) additionalInformation.get(AdditionalInformationTokenEnhancer.SYSTEM_INFO));
            dtos.add(dto);
        });

        return dtos.stream().sorted((t1, t2) -> {
            if (isNull(t1.getCreatedAt()) && nonNull(t2.getCreatedAt())) {
                return 1;
            }

            if (isNull(t1.getCreatedAt()) && isNull(t2.getCreatedAt())) {
                return 0;
            }

            if (isNull(t2.getCreatedAt())) {
                return -1;
            }

            return t2.getCreatedAt().compareTo(t1.getCreatedAt());
        }).collect(Collectors.toList());
    }

    public void clearSessionsExceptCurrent() {
        authSessionService.clearAllSessionsExceptCurrent();
    }
}
