package ru.diamondshield_central.dto.schedule;

import ru.diamondshield_central.entity.Schedule;
import ru.diamondshield_central.entity.ScheduleInterval;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ScheduleResponse {

    private UUID id;
    private UUID organizationId;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ScheduleIntervalResponse> intervals;

    public ScheduleResponse() {
    }

    public static ScheduleResponse fromEntity(Schedule schedule, List<ScheduleInterval> intervals) {
        ScheduleResponse response = new ScheduleResponse();

        response.id = schedule.getId();

        if (schedule.getOrganization() != null) {
            response.organizationId = schedule.getOrganization().getId();
        }

        response.name = schedule.getName();
        response.description = schedule.getDescription();
        response.active = schedule.getActive();
        response.createdAt = schedule.getCreatedAt();
        response.updatedAt = schedule.getUpdatedAt();

        // Интервалы преобразуем в DTO отдельно
        response.intervals = intervals.stream()
                .map(ScheduleIntervalResponse::fromEntity)
                .toList();

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

    public List<ScheduleIntervalResponse> getIntervals() {
        return intervals;
    }
}