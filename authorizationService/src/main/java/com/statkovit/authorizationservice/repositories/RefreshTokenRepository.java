package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccountUuid(UUID uuid);

    Optional<RefreshToken> findByOpaqueToken(String opaqueToken);

}
