package com.statkovit.authorizationservice.services.impl;

import com.statkovit.authorizationservice.repositories.AuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAuthClientDetailsService implements ClientDetailsService {

    private final AuthClientRepository authClientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return authClientRepository.findByClientId(clientId).orElseThrow(
                () -> new IllegalArgumentException(String.format("Client with id %s is not found!", clientId))
        );
    }
}
