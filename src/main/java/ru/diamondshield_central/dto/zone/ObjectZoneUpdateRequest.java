package ru.diamondshield_central.dto.zone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ObjectZoneUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String zoneType;

    private String description;

    public ObjectZoneUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}