package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.AccessRule;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalConfigAccessRuleDto {

    private UUID id;
    private UUID personId;
    private UUID accessPointId;
    private UUID scheduleId;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean active;

    public static LocalConfigAccessRuleDto fromEntity(AccessRule rule) {
        LocalConfigAccessRuleDto dto = new LocalConfigAccessRuleDto();

        dto.id = rule.getId();
        dto.personId = rule.getPerson().getId();
        dto.accessPointId = rule.getAccessPoint().getId();

        if (rule.getSchedule() != null) {
            dto.scheduleId = rule.getSchedule().getId();
        }

        dto.validFrom = rule.getValidFrom();
        dto.validTo = rule.getValidTo();
        dto.active = rule.getActive();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public Boolean getActive() {
        return active;
    }
}