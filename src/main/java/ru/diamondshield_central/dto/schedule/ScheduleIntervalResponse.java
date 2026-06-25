package ru.diamondshield_central.dto.schedule;

import ru.diamondshield_central.entity.ScheduleInterval;

import java.time.LocalTime;
import java.util.UUID;

public class ScheduleIntervalResponse {

    private UUID id;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ScheduleIntervalResponse() {
    }

    public static ScheduleIntervalResponse fromEntity(ScheduleInterval interval) {
        ScheduleIntervalResponse response = new ScheduleIntervalResponse();

        // Отдаем только поля интервала без всего объекта расписания
        response.id = interval.getId();
        response.dayOfWeek = interval.getDayOfWeek();
        response.startTime = interval.getStartTime();
        response.endTime = interval.getEndTime();

        return response;
    }

    public UUID getId() {
        return id;
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