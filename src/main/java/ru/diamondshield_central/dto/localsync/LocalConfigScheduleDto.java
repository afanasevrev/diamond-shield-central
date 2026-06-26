package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.Schedule;

import java.util.UUID;

public class LocalConfigScheduleDto {

    private UUID id;
    private String name;
    private Boolean active;

    public static LocalConfigScheduleDto fromEntity(Schedule schedule) {
        LocalConfigScheduleDto dto = new LocalConfigScheduleDto();

        dto.id = schedule.getId();
        dto.name = schedule.getName();
        dto.active = schedule.getActive();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }
}