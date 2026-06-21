package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "import_history_details")
public class ImportHistoryDetail {

    @Id
    @GeneratedValue
    private UUID id;

    // Ссылка на общую операцию импорта
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "import_history_id", nullable = false)
    private ImportHistory importHistory;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    // added, skipped, error
    @Column(nullable = false, length = 50)
    private String status;

    @Column(columnDefinition = "text")
    private String reason;

    // JSONB с исходными данными строки
    @Column(name = "raw_data", columnDefinition = "jsonb")
    private String rawData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ImportHistoryDetail() {
    }

    @PrePersist
    public void prePersist() {
        // Фиксируем дату создания детали импорта
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public ImportHistory getImportHistory() {
        return importHistory;
    }

    public void setImportHistory(ImportHistory importHistory) {
        this.importHistory = importHistory;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}