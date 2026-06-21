package ru.diamondshield_central.dto.accesspoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AccessPointCreateRequest {

    @NotNull
    private UUID objectId;

    private UUID zoneFromId;

    private UUID zoneToId;

    private UUID controllerId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String accessPointType;

    public AccessPointCreateRequest() {
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public UUID getZoneFromId() {
        return zoneFromId;
    }

    public void setZoneFromId(UUID zoneFromId) {
        this.zoneFromId = zoneFromId;
    }

    public UUID getZoneToId() {
        return zoneToId;
    }

    public void setZoneToId(UUID zoneToId) {
        this.zoneToId = zoneToId;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public void setControllerId(UUID controllerId) {
        this.controllerId = controllerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessPointType() {
        return accessPointType;
    }

    public void setAccessPointType(String accessPointType) {
        this.accessPointType = accessPointType;
    }
}