package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.domain.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

    Optional<AccessToken> findByAccountUuid(UUID uuid);

    Optional<AccessToken> findByOpaqueToken(String opaqueToken);
}
