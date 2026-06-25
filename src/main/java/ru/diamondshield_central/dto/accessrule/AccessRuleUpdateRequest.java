package ru.diamondshield_central.dto.accessrule;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessRuleUpdateRequest {

    private UUID scheduleId;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private Boolean active;

    public AccessRuleUpdateRequest() {
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(UUID scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}