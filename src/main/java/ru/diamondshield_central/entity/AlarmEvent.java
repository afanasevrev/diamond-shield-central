package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alarm_events")
public class AlarmEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_server_id")
    private LocalServer localServer;

    // Используется для дедупликации тревог от локального сервера
    @Column(name = "local_event_id", length = 100)
    private String localEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private AccessObject object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controller_id")
    private ControllerDevice controller;

    @Column(name = "alarm_type", nullable = false, length = 100)
    private String alarmType;

    // low, medium, high, critical
    @Column(nullable = false, length = 50)
    private String severity;

    @Column(columnDefinition = "text")
    private String message;

    // new, acknowledged, resolved
    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acknowledged_by")
    private SystemUser acknowledgedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private SystemUser resolvedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AlarmEvent() {
    }

    @PrePersist
    public void prePersist() {
        // created_at — момент регистрации тревоги в центральной БД
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "new";
        }
    }

    public UUID getId() {
        return id;
    }

    public LocalServer getLocalServer() {
        return localServer;
    }

    public void setLocalServer(LocalServer localServer) {
        this.localServer = localServer;
    }

    public String getLocalEventId() {
        return localEventId;
    }

    public void setLocalEventId(String localEventId) {
        this.localEventId = localEventId;
    }

    public AccessObject getObject() {
        return object;
    }

    public void setObject(AccessObject object) {
        this.object = object;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public ControllerDevice getController() {
        return controller;
    }

    public void setController(ControllerDevice controller) {
        this.controller = controller;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public SystemUser getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(SystemUser acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public SystemUser getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(SystemUser resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}