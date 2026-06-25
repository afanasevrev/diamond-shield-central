package ru.diamondshield_central.dto.localsync;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalAlarmEventPushRequest {

    @NotBlank
    @Size(max = 100)
    private String localEventId;

    private UUID objectId;
    private UUID accessPointId;
    private UUID readerId;
    private UUID controllerId;

    @NotBlank
    @Size(max = 100)
    private String alarmType;

    @NotBlank
    @Size(max = 50)
    private String severity;

    private String message;

    @NotNull
    private LocalDateTime occurredAt;

    public LocalAlarmEventPushRequest() {
    }

    public String getLocalEventId() {
        return localEventId;
    }

    public void setLocalEventId(String localEventId) {
        this.localEventId = localEventId;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(UUID accessPointId) {
        this.accessPointId = accessPointId;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public void setReaderId(UUID readerId) {
        this.readerId = readerId;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public void setControllerId(UUID controllerId) {
        this.controllerId = controllerId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}