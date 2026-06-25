package ru.diamondshield_central.dto.localsync;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalAccessEventPushRequest {

    @NotBlank
    @Size(max = 100)
    private String localEventId;

    private UUID objectId;
    private UUID accessPointId;
    private UUID readerId;
    private UUID controllerId;
    private UUID personId;
    private UUID identifierId;

    @NotNull
    private LocalDateTime eventTime;

    @Size(max = 20)
    private String direction;

    @NotBlank
    @Size(max = 50)
    private String accessResult;

    @Size(max = 255)
    private String reason;

    @Size(max = 50)
    private String identifierType;

    @Size(max = 255)
    private String identifierValue;

    public LocalAccessEventPushRequest() {
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

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public UUID getIdentifierId() {
        return identifierId;
    }

    public void setIdentifierId(UUID identifierId) {
        this.identifierId = identifierId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAccessResult() {
        return accessResult;
    }

    public void setAccessResult(String accessResult) {
        this.accessResult = accessResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }
}