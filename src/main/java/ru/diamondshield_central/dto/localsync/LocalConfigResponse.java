package ru.diamondshield_central.dto.localsync;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LocalConfigResponse {

    private UUID localServerId;
    private UUID objectId;
    private LocalDateTime generatedAt;

    private List<LocalConfigControllerDto> controllers;
    private List<LocalConfigReaderDto> readers;
    private List<LocalConfigAccessPointDto> accessPoints;
    private List<LocalConfigPersonDto> persons;
    private List<LocalConfigIdentifierDto> identifiers;
    private List<LocalConfigAccessRuleDto> accessRules;
    private List<LocalConfigScheduleDto> schedules;
    private List<LocalConfigScheduleIntervalDto> scheduleIntervals;

    public LocalConfigResponse() {
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public void setLocalServerId(UUID localServerId) {
        this.localServerId = localServerId;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<LocalConfigControllerDto> getControllers() {
        return controllers;
    }

    public void setControllers(List<LocalConfigControllerDto> controllers) {
        this.controllers = controllers;
    }

    public List<LocalConfigReaderDto> getReaders() {
        return readers;
    }

    public void setReaders(List<LocalConfigReaderDto> readers) {
        this.readers = readers;
    }

    public List<LocalConfigAccessPointDto> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<LocalConfigAccessPointDto> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public List<LocalConfigPersonDto> getPersons() {
        return persons;
    }

    public void setPersons(List<LocalConfigPersonDto> persons) {
        this.persons = persons;
    }

    public List<LocalConfigIdentifierDto> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<LocalConfigIdentifierDto> identifiers) {
        this.identifiers = identifiers;
    }

    public List<LocalConfigAccessRuleDto> getAccessRules() {
        return accessRules;
    }

    public void setAccessRules(List<LocalConfigAccessRuleDto> accessRules) {
        this.accessRules = accessRules;
    }

    public List<LocalConfigScheduleDto> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<LocalConfigScheduleDto> schedules) {
        this.schedules = schedules;
    }

    public List<LocalConfigScheduleIntervalDto> getScheduleIntervals() {
        return scheduleIntervals;
    }

    public void setScheduleIntervals(List<LocalConfigScheduleIntervalDto> scheduleIntervals) {
        this.scheduleIntervals = scheduleIntervals;
    }
}