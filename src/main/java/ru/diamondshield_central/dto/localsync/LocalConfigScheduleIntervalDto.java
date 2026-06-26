package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.ScheduleInterval;

import java.time.LocalTime;
import java.util.UUID;

public class LocalConfigScheduleIntervalDto {

    private UUID id;
    private UUID scheduleId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static LocalConfigScheduleIntervalDto fromEntity(ScheduleInterval interval) {
        LocalConfigScheduleIntervalDto dto = new LocalConfigScheduleIntervalDto();

        dto.id = interval.getId();
        dto.scheduleId = interval.getSchedule().getId();
        dto.dayOfWeek = interval.getDayOfWeek();
        dto.startTime = interval.getStartTime();
        dto.endTime = interval.getEndTime();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}