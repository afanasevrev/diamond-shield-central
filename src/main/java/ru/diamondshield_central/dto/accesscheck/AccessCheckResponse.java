package ru.diamondshield_central.dto.accesscheck;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessCheckResponse {

    private String decision;
    private Boolean allowed;
    private String reason;

    private UUID personId;
    private String personFullName;

    private UUID identifierId;
    private String identifierMasked;

    private UUID accessPointId;
    private UUID accessRuleId;
    private UUID scheduleId;

    private UUID accessEventId;

    private LocalDateTime checkedAt;

    public AccessCheckResponse() {
    }

    public static AccessCheckResponse of(String decision,
                                         Boolean allowed,
                                         String reason,
                                         UUID personId,
                                         String personFullName,
                                         UUID identifierId,
                                         String identifierMasked,
                                         UUID accessPointId,
                                         UUID accessRuleId,
                                         UUID scheduleId,
                                         UUID accessEventId,
                                         LocalDateTime checkedAt) {
        AccessCheckResponse response = new AccessCheckResponse();

        // decision — машинно-читаемый результат, reason — человекочитаемая причина
        response.decision = decision;
        response.allowed = allowed;
        response.reason = reason;
        response.personId = personId;
        response.personFullName = personFullName;
        response.identifierId = identifierId;
        response.identifierMasked = identifierMasked;
        response.accessPointId = accessPointId;
        response.accessRuleId = accessRuleId;
        response.scheduleId = scheduleId;
        response.accessEventId = accessEventId;
        response.checkedAt = checkedAt;

        return response;
    }

    public String getDecision() {
        return decision;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public String getReason() {
        return reason;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public UUID getIdentifierId() {
        return identifierId;
    }

    public String getIdentifierMasked() {
        return identifierMasked;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public UUID getAccessRuleId() {
        return accessRuleId;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public UUID getAccessEventId() {
        return accessEventId;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}