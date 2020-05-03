package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.entities.Role;
import com.statkovit.authorizationservice.enums.RoleName;
import com.statkovit.authorizationservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getAccountOwnerRole() {
        return getRoleByAuthority(RoleName.ACCOUNT_OWNER);
    }

    private Role getRoleByAuthority(RoleName authority) {
        return roleRepository.getByAuthority(authority).orElseThrow(() ->
                new EntityNotFoundException(String.format("Role with authority %s is not found.", authority))
        );
    }
}
