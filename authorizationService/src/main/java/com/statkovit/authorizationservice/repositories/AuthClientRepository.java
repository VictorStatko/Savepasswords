package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.domain.AuthClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthClientRepository extends JpaRepository<AuthClientDetail, Long> {

    Optional<AuthClientDetail> findByClientId(String clientId);

}
