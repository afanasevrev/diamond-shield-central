package ru.diamondshield_central.dto.sync;

import ru.diamondshield_central.entity.SyncHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public class SyncHistoryResponse {

    private UUID id;
    private UUID localServerId;
    private UUID objectId;
    private String syncType;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private Integer totalItems;
    private Integer acceptedItems;
    private Integer skippedItems;
    private Integer errorItems;
    private String errorMessage;

    public SyncHistoryResponse() {
    }

    public static SyncHistoryResponse fromEntity(SyncHistory history) {
        SyncHistoryResponse response = new SyncHistoryResponse();

        // Entity преобразуем в DTO без передачи Hibernate-прокси наружу
        response.id = history.getId();

        if (history.getLocalServer() != null) {
            response.localServerId = history.getLocalServer().getId();
        }

        if (history.getObject() != null) {
            response.objectId = history.getObject().getId();
        }

        response.syncType = history.getSyncType();
        response.status = history.getStatus();
        response.requestedAt = history.getRequestedAt();
        response.completedAt = history.getCompletedAt();
        response.totalItems = history.getTotalItems();
        response.acceptedItems = history.getAcceptedItems();
        response.skippedItems = history.getSkippedItems();
        response.errorItems = history.getErrorItems();
        response.errorMessage = history.getErrorMessage();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public String getSyncType() {
        return syncType;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public Integer getAcceptedItems() {
        return acceptedItems;
    }

    public Integer getSkippedItems() {
        return skippedItems;
    }

    public Integer getErrorItems() {
        return errorItems;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}