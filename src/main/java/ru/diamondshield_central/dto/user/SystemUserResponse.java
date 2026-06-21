package ru.diamondshield_central.dto.user;

import ru.diamondshield_central.entity.SystemUser;

import java.time.LocalDateTime;
import java.util.UUID;

public class SystemUserResponse {

    private UUID id;
    private UUID organizationId;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private Boolean active;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SystemUserResponse() {
    }

    public static SystemUserResponse fromEntity(SystemUser user) {
        SystemUserResponse response = new SystemUserResponse();
        response.id = user.getId();

        if (user.getOrganization() != null) {
            response.organizationId = user.getOrganization().getId();
        }

        response.username = user.getUsername();
        response.email = user.getEmail();
        response.phone = user.getPhone();
        response.fullName = user.getFullName();
        response.active = user.getActive();
        response.lastLoginAt = user.getLastLoginAt();
        response.createdAt = user.getCreatedAt();
        response.updatedAt = user.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullName;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}