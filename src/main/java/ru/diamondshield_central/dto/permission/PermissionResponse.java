package ru.diamondshield_central.dto.permission;

import ru.diamondshield_central.entity.Permission;

import java.util.UUID;

public class PermissionResponse {

    private UUID id;
    private String code;
    private String name;
    private String description;

    public PermissionResponse() {
    }

    public static PermissionResponse fromEntity(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.id = permission.getId();
        response.code = permission.getCode();
        response.name = permission.getName();
        response.description = permission.getDescription();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}