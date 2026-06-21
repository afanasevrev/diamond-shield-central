package ru.diamondshield_central.dto.accesspoint;

import ru.diamondshield_central.entity.AccessPoint;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessPointResponse {

    private UUID id;
    private UUID objectId;
    private UUID zoneFromId;
    private UUID zoneToId;
    private UUID controllerId;
    private String name;
    private String accessPointType;
    private String status;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccessPointResponse() {
    }

    public static AccessPointResponse fromEntity(AccessPoint accessPoint) {
        AccessPointResponse response = new AccessPointResponse();
        response.id = accessPoint.getId();
        response.objectId = accessPoint.getObject().getId();

        if (accessPoint.getZoneFrom() != null) {
            response.zoneFromId = accessPoint.getZoneFrom().getId();
        }

        if (accessPoint.getZoneTo() != null) {
            response.zoneToId = accessPoint.getZoneTo().getId();
        }

        if (accessPoint.getController() != null) {
            response.controllerId = accessPoint.getController().getId();
        }

        response.name = accessPoint.getName();
        response.accessPointType = accessPoint.getAccessPointType();
        response.status = accessPoint.getStatus();
        response.active = accessPoint.getActive();
        response.createdAt = accessPoint.getCreatedAt();
        response.updatedAt = accessPoint.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public UUID getZoneFromId() {
        return zoneFromId;
    }

    public UUID getZoneToId() {
        return zoneToId;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public String getName() {
        return name;
    }

    public String getAccessPointType() {
        return accessPointType;
    }

    public String getStatus() {
        return status;
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