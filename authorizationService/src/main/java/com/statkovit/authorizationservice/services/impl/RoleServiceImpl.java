package com.statkovit.authorizationservice.services.impl;

import com.statkovit.authorizationservice.domain.Role;
import com.statkovit.authorizationservice.enums.RoleName;
import com.statkovit.authorizationservice.repositories.RoleRepository;
import com.statkovit.authorizationservice.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Role getAccountOwnerRole() {
        return getRoleByAuthority(RoleName.ACCOUNT_OWNER);
    }

    private Role getRoleByAuthority(RoleName authority) {
        return roleRepository.getByAuthority(authority).orElseThrow(() ->
                new EntityNotFoundException(String.format("Role with authority %s is not found.", authority))
        );
    }
}
