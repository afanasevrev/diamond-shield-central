package ru.diamondshield_central.dto.localsync;

import java.util.UUID;

public class LocalBatchPushResponse {

    private UUID syncHistoryId;
    private String status;
    private int totalItems;
    private int acceptedItems;
    private int skippedItems;
    private int errorItems;

    public LocalBatchPushResponse() {
    }

    public UUID getSyncHistoryId() {
        return syncHistoryId;
    }

    public void setSyncHistoryId(UUID syncHistoryId) {
        this.syncHistoryId = syncHistoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // success, partial, error
        this.status = status;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getAcceptedItems() {
        return acceptedItems;
    }

    public void setAcceptedItems(int acceptedItems) {
        this.acceptedItems = acceptedItems;
    }

    public int getSkippedItems() {
        return skippedItems;
    }

    public void setSkippedItems(int skippedItems) {
        this.skippedItems = skippedItems;
    }

    public int getErrorItems() {
        return errorItems;
    }

    public void setErrorItems(int errorItems) {
        this.errorItems = errorItems;
    }
}