package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "import_history")
public class ImportHistory {

    @Id
    @GeneratedValue
    private UUID id;

    // Организация, в рамках которой выполнялся импорт
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    // Пользователь системы, который загрузил файл
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_user_id")
    private SystemUser systemUser;

    @Column(name = "file_name", length = 255)
    private String fileName;

    // Например: persons_xlsx
    @Column(name = "import_type", nullable = false, length = 100)
    private String importType;

    @Column(name = "total_rows", nullable = false)
    private Integer totalRows;

    @Column(name = "success_rows", nullable = false)
    private Integer successRows;

    @Column(name = "skipped_rows", nullable = false)
    private Integer skippedRows;

    // success, partial, error
    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ImportHistory() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        // Значения по умолчанию защищают от null
        if (this.totalRows == null) {
            this.totalRows = 0;
        }

        if (this.successRows == null) {
            this.successRows = 0;
        }

        if (this.skippedRows == null) {
            this.skippedRows = 0;
        }
    }

    public UUID getId() {
        return id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getSuccessRows() {
        return successRows;
    }

    public void setSuccessRows(Integer successRows) {
        this.successRows = successRows;
    }

    public Integer getSkippedRows() {
        return skippedRows;
    }

    public void setSkippedRows(Integer skippedRows) {
        this.skippedRows = skippedRows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}