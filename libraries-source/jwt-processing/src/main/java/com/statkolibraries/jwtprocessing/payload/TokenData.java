package com.statkolibraries.jwtprocessing.payload;

import io.jsonwebtoken.Claims;

import java.util.*;

public class TokenData {
    private static final String ID_CLAIM = "id";
    private static final String UUID_CLAIM = "uuid";
    private static final String ROLES_CLAIM = "roles";
    private static final String PERMISSIONS_CLAIM = "permissions";

    private final Long id;

    private final UUID uuid;

    private final List<String> roles;

    private final List<String> permissions;

    public TokenData(Long id, UUID uuid, List<String> roles, List<String> permissions) {
        this.id = id;
        this.uuid = uuid;
        this.roles = roles == null ? new ArrayList<>() : roles;
        this.permissions = permissions == null ? new ArrayList<>() : permissions;
    }

    public Map<String, Object> toClaimMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_CLAIM, id);
        claims.put(UUID_CLAIM, uuid);
        claims.put(ROLES_CLAIM, roles);
        claims.put(PERMISSIONS_CLAIM, permissions);
        return claims;
    }

    public static TokenData fromClaimsMap(Claims claims) {
        Long id = claims.get(ID_CLAIM, Long.class);

        UUID uuid = Optional.ofNullable(claims.get(UUID_CLAIM, String.class))
                .map(UUID::fromString)
                .orElse(null);

        List<String> roles = claims.get(ROLES_CLAIM, List.class);
        List<String> permissions = claims.get(PERMISSIONS_CLAIM, List.class);
        return new TokenData(id, uuid, roles, permissions);
    }


    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
