package ru.diamondshield_central.dto.accessrule;

import ru.diamondshield_central.entity.AccessRule;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessRuleResponse {

    private UUID id;
    private UUID personId;
    private String personFullName;
    private UUID accessPointId;
    private String accessPointName;
    private UUID scheduleId;
    private String scheduleName;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccessRuleResponse() {
    }

    public static AccessRuleResponse fromEntity(AccessRule rule) {
        AccessRuleResponse response = new AccessRuleResponse();

        response.id = rule.getId();

        if (rule.getPerson() != null) {
            response.personId = rule.getPerson().getId();
            response.personFullName = rule.getPerson().getLastName() + " " + rule.getPerson().getFirstName();
        }

        if (rule.getAccessPoint() != null) {
            response.accessPointId = rule.getAccessPoint().getId();
            response.accessPointName = rule.getAccessPoint().getName();
        }

        if (rule.getSchedule() != null) {
            response.scheduleId = rule.getSchedule().getId();
            response.scheduleName = rule.getSchedule().getName();
        }

        response.validFrom = rule.getValidFrom();
        response.validTo = rule.getValidTo();
        response.active = rule.getActive();
        response.createdAt = rule.getCreatedAt();
        response.updatedAt = rule.getUpdatedAt();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}