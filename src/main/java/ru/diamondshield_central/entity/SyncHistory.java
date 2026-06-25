package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sync_history")
public class SyncHistory {

    @Id
    @GeneratedValue
    private UUID id;

    // Локальный сервер, который выполнял синхронизацию
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_server_id", nullable = false)
    private LocalServer localServer;

    // Объект, к которому относится синхронизация
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private AccessObject object;

    // Тип синхронизации: config_pull, access_events_push, alarm_events_push, device_status_push
    @Column(name = "sync_type", nullable = false, length = 100)
    private String syncType;

    // processing, success, partial, error
    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "total_items", nullable = false)
    private Integer totalItems;

    @Column(name = "accepted_items", nullable = false)
    private Integer acceptedItems;

    @Column(name = "skipped_items", nullable = false)
    private Integer skippedItems;

    @Column(name = "error_items", nullable = false)
    private Integer errorItems;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    public SyncHistory() {
    }

    @PrePersist
    public void prePersist() {
        // requested_at фиксируется в момент создания записи
        if (this.requestedAt == null) {
            this.requestedAt = LocalDateTime.now();
        }

        // Начальный статус синхронизации
        if (this.status == null) {
            this.status = "processing";
        }

        if (this.totalItems == null) {
            this.totalItems = 0;
        }

        if (this.acceptedItems == null) {
            this.acceptedItems = 0;
        }

        if (this.skippedItems == null) {
            this.skippedItems = 0;
        }

        if (this.errorItems == null) {
            this.errorItems = 0;
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

    public AccessObject getObject() {
        return object;
    }

    public void setObject(AccessObject object) {
        this.object = object;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getAcceptedItems() {
        return acceptedItems;
    }

    public void setAcceptedItems(Integer acceptedItems) {
        this.acceptedItems = acceptedItems;
    }

    public Integer getSkippedItems() {
        return skippedItems;
    }

    public void setSkippedItems(Integer skippedItems) {
        this.skippedItems = skippedItems;
    }

    public Integer getErrorItems() {
        return errorItems;
    }

    public void setErrorItems(Integer errorItems) {
        this.errorItems = errorItems;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}