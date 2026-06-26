package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.AccessPoint;

import java.util.UUID;

public class LocalConfigAccessPointDto {

    private UUID id;
    private UUID controllerId;
    private UUID zoneFromId;
    private UUID zoneToId;
    private String name;
    private String accessPointType;
    private Boolean active;

    public static LocalConfigAccessPointDto fromEntity(AccessPoint accessPoint) {
        LocalConfigAccessPointDto dto = new LocalConfigAccessPointDto();

        dto.id = accessPoint.getId();

        if (accessPoint.getController() != null) {
            dto.controllerId = accessPoint.getController().getId();
        }

        if (accessPoint.getZoneFrom() != null) {
            dto.zoneFromId = accessPoint.getZoneFrom().getId();
        }

        if (accessPoint.getZoneTo() != null) {
            dto.zoneToId = accessPoint.getZoneTo().getId();
        }

        dto.name = accessPoint.getName();
        dto.accessPointType = accessPoint.getAccessPointType();
        dto.active = accessPoint.getActive();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public UUID getZoneFromId() {
        return zoneFromId;
    }

    public UUID getZoneToId() {
        return zoneToId;
    }

    public String getName() {
        return name;
    }

    public String getAccessPointType() {
        return accessPointType;
    }

    public Boolean getActive() {
        return active;
    }
}