package com.statkolibraries.jwtprocessing.payload;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TokenData {
    private static final String LIST_DELIMITER = " , ";

    private final Long id;

    private final UUID uuid;

    private final List<String> roles;

    private final List<String> permissions;

    public TokenData(Long id, UUID uuid, List<String> roles, List<String> permissions) {
        this.id = id;
        this.uuid = uuid;
        this.roles = roles;
        this.permissions = permissions;
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

        jsonObject.put("id", id);
        jsonObject.put("uuid", uuid);
        jsonObject.put("roles", String.join(LIST_DELIMITER, roles));
        jsonObject.put("permissions", String.join(LIST_DELIMITER, permissions));

        return jsonObject;
    }

    public static TokenData fromJson(JSONObject jsonObject) {
        Long id = (Long) jsonObject.getAsNumber("id");
        UUID uuid = UUID.fromString(jsonObject.getAsString("uuid"));
        String rolesString = jsonObject.getAsString("roles");
        List<String> roles = new ArrayList<>(Arrays.asList(rolesString.split(LIST_DELIMITER)));
        String permissionsString = jsonObject.getAsString("permissions");
        List<String> permissions = new ArrayList<>(Arrays.asList(permissionsString.split(LIST_DELIMITER)));
        return new TokenData(id, uuid, roles, permissions);
    }
}
