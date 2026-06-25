package ru.diamondshield_central.dto.schedule;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ScheduleUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    private Boolean active;

    @Valid
    private List<ScheduleIntervalRequest> intervals;

    public ScheduleUpdateRequest() {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ScheduleIntervalRequest> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<ScheduleIntervalRequest> intervals) {
        this.intervals = intervals;
    }
}