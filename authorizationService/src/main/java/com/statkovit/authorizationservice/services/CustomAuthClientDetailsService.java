package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.repositories.AuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthClientDetailsService implements ClientDetailsService {

    private final AuthClientRepository authClientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return authClientRepository.findByClientId(clientId).orElseThrow(
                () -> new NoSuchClientException(String.format("Client with id %s is not found!", clientId))
        );
    }
}
