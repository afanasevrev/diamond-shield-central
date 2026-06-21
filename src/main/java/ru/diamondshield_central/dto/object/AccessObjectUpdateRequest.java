package ru.diamondshield_central.dto.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AccessObjectUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String objectType;

    private String address;

    @Size(max = 50)
    private String timezone;

    private UUID workScheduleId;

    private String description;

    private Boolean active;

    public AccessObjectUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public UUID getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(UUID workScheduleId) {
        this.workScheduleId = workScheduleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}