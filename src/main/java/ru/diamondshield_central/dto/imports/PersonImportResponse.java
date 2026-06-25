package ru.diamondshield_central.dto.imports;

import java.util.UUID;

public class PersonImportResponse {

    private UUID importHistoryId;
    private String status;
    private int totalRows;
    private int successRows;
    private int skippedRows;
    private String message;

    public PersonImportResponse() {
    }

    public UUID getImportHistoryId() {
        return importHistoryId;
    }

    public void setImportHistoryId(UUID importHistoryId) {
        this.importHistoryId = importHistoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // success, partial, error
        this.status = status;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getSuccessRows() {
        return successRows;
    }

    public void setSuccessRows(int successRows) {
        this.successRows = successRows;
    }

    public int getSkippedRows() {
        return skippedRows;
    }

    public void setSkippedRows(int skippedRows) {
        this.skippedRows = skippedRows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}