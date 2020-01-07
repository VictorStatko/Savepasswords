package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.domain.Role;
import com.statkovit.authorizationservice.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getByAuthority(RoleName authority);

}
