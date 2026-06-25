package ru.diamondshield_central.dto.schedule;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class ScheduleCreateRequest {

    @NotNull
    private UUID organizationId;

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @Valid
    private List<ScheduleIntervalRequest> intervals;

    public ScheduleCreateRequest() {
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ScheduleIntervalRequest> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<ScheduleIntervalRequest> intervals) {
        this.intervals = intervals;
    }
}