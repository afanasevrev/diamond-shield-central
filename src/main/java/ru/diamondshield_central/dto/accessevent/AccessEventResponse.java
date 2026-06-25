package ru.diamondshield_central.dto.accessevent;

import ru.diamondshield_central.entity.AccessEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessEventResponse {

    private UUID id;
    private UUID localServerId;
    private String localEventId;
    private UUID objectId;
    private UUID accessPointId;
    private UUID readerId;
    private UUID controllerId;
    private UUID personId;
    private UUID identifierId;
    private LocalDateTime eventTime;
    private String direction;
    private String accessResult;
    private String reason;
    private String identifierType;
    private String identifierMasked;
    private Boolean unknownIdentifier;
    private LocalDateTime createdAt;

    public AccessEventResponse() {
    }

    public static AccessEventResponse fromEntity(AccessEvent event) {
        AccessEventResponse response = new AccessEventResponse();

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

        if (event.getPerson() != null) {
            response.personId = event.getPerson().getId();
        }

        if (event.getIdentifier() != null) {
            response.identifierId = event.getIdentifier().getId();
        }

        response.eventTime = event.getEventTime();
        response.direction = event.getDirection();
        response.accessResult = event.getAccessResult();
        response.reason = event.getReason();
        response.identifierType = event.getIdentifierType();
        response.identifierMasked = event.getIdentifierMasked();
        response.unknownIdentifier = event.getUnknownIdentifier();
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

    public UUID getPersonId() {
        return personId;
    }

    public UUID getIdentifierId() {
        return identifierId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public String getDirection() {
        return direction;
    }

    public String getAccessResult() {
        return accessResult;
    }

    public String getReason() {
        return reason;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public String getIdentifierMasked() {
        return identifierMasked;
    }

    public Boolean getUnknownIdentifier() {
        return unknownIdentifier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}