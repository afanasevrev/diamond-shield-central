package ru.diamondshield_central.dto.alarmevent;

import ru.diamondshield_central.entity.AlarmEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class AlarmEventResponse {

    private UUID id;
    private UUID localServerId;
    private String localEventId;
    private UUID objectId;
    private UUID accessPointId;
    private UUID readerId;
    private UUID controllerId;
    private String alarmType;
    private String severity;
    private String message;
    private String status;
    private LocalDateTime occurredAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private UUID acknowledgedBy;
    private UUID resolvedBy;
    private LocalDateTime createdAt;

    public AlarmEventResponse() {
    }

    public static AlarmEventResponse fromEntity(AlarmEvent event) {
        AlarmEventResponse response = new AlarmEventResponse();

        response.id = event.getId();

        if (event.getLocalServer() != null) {
            response.localServerId = event.getLocalServer().getId();
        }

        response.localEventId = event.getLocalEventId();

        if (event.getObject() != null) {
            response.objectId = event.getObject().getId();
        }

        if (event.getAccessPoint() != null) {
            response.accessPointId = event.getAccessPoint().getId();
        }

        if (event.getReader() != null) {
            response.readerId = event.getReader().getId();
        }

        if (event.getController() != null) {
            response.controllerId = event.getController().getId();
        }

        if (event.getAcknowledgedBy() != null) {
            response.acknowledgedBy = event.getAcknowledgedBy().getId();
        }

        if (event.getResolvedBy() != null) {
            response.resolvedBy = event.getResolvedBy().getId();
        }

        response.alarmType = event.getAlarmType();
        response.severity = event.getSeverity();
        response.message = event.getMessage();
        response.status = event.getStatus();
        response.occurredAt = event.getOccurredAt();
        response.acknowledgedAt = event.getAcknowledgedAt();
        response.resolvedAt = event.getResolvedAt();
        response.createdAt = event.getCreatedAt();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public String getLocalEventId() {
        return localEventId;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public UUID getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public UUID getResolvedBy() {
        return resolvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}