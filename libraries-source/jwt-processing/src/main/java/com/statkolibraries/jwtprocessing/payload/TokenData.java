package com.statkolibraries.jwtprocessing.payload;

import net.minidev.json.JSONObject;

import java.util.*;

public class TokenData {
    private static final String LIST_DELIMITER = " , ";
    private static final String ID_JSON_FIELD = "id";
    private static final String UUID_JSON_FIELD = "uuid";
    private static final String ROLES_JSON_FIELD = "roles";
    private static final String PERMISSIONS_JSON_FIELD = "permissions";
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

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID_JSON_FIELD, id);
        jsonObject.put(UUID_JSON_FIELD, uuid);
        jsonObject.put(ROLES_JSON_FIELD, String.join(LIST_DELIMITER, roles));
        jsonObject.put(PERMISSIONS_JSON_FIELD, String.join(LIST_DELIMITER, permissions));

        return jsonObject;
    }

    public static TokenData fromJson(JSONObject jsonObject) {
        Long id = Optional.ofNullable(jsonObject.getAsNumber(ID_JSON_FIELD)).map(Number::longValue).orElse(null);
        UUID uuid = Optional.ofNullable(jsonObject.getAsString(UUID_JSON_FIELD)).map(UUID::fromString).orElse(null);
        String rolesString = jsonObject.getAsString(ROLES_JSON_FIELD);
        List<String> roles;
        if (rolesString == null || rolesString.isEmpty()) {
            roles = new ArrayList<>();
        } else {
            roles = new ArrayList<>(Arrays.asList(rolesString.split(LIST_DELIMITER)));
        }
        String permissionsString = jsonObject.getAsString(PERMISSIONS_JSON_FIELD);
        List<String> permissions;
        if (permissionsString == null || permissionsString.isEmpty()) {
            permissions = new ArrayList<>();
        } else {
            permissions = new ArrayList<>(Arrays.asList(permissionsString.split(LIST_DELIMITER)));
            ;
        }
        return new TokenData(id, uuid, roles, permissions);
    }
}
