package ru.diamondshield_central.dto.object;

import ru.diamondshield_central.entity.AccessObject;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessObjectResponse {

    private UUID id;
    private UUID organizationId;
    private String name;
    private String objectType;
    private String address;
    private String timezone;
    private UUID workScheduleId;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccessObjectResponse() {
    }

    public static AccessObjectResponse fromEntity(AccessObject object) {
        AccessObjectResponse response = new AccessObjectResponse();
        response.id = object.getId();
        response.organizationId = object.getOrganization().getId();
        response.name = object.getName();
        response.objectType = object.getObjectType();
        response.address = object.getAddress();
        response.timezone = object.getTimezone();
        response.workScheduleId = object.getWorkScheduleId();
        response.description = object.getDescription();
        response.active = object.getActive();
        response.createdAt = object.getCreatedAt();
        response.updatedAt = object.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getAddress() {
        return address;
    }

    public String getTimezone() {
        return timezone;
    }

    public UUID getWorkScheduleId() {
        return workScheduleId;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}