package ru.diamondshield_central.dto.role;

import ru.diamondshield_central.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class RoleResponse {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public RoleResponse() {
    }

    public static RoleResponse fromEntity(Role role) {
        RoleResponse response = new RoleResponse();
        response.id = role.getId();
        response.code = role.getCode();
        response.name = role.getName();
        response.description = role.getDescription();
        response.createdAt = role.getCreatedAt();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}