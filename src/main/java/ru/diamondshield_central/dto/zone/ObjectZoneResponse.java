package ru.diamondshield_central.dto.zone;

import ru.diamondshield_central.entity.ObjectZone;

import java.time.LocalDateTime;
import java.util.UUID;

public class ObjectZoneResponse {

    private UUID id;
    private UUID objectId;
    private String name;
    private String zoneType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ObjectZoneResponse() {
    }

    public static ObjectZoneResponse fromEntity(ObjectZone zone) {
        ObjectZoneResponse response = new ObjectZoneResponse();
        response.id = zone.getId();
        response.objectId = zone.getObject().getId();
        response.name = zone.getName();
        response.zoneType = zone.getZoneType();
        response.description = zone.getDescription();
        response.createdAt = zone.getCreatedAt();
        response.updatedAt = zone.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public String getZoneType() {
        return zoneType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}