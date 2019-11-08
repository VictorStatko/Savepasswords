package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    List<RefreshToken> findByAccountUuid(UUID uuid);
}
