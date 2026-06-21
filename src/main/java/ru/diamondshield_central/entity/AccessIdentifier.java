package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_identifiers")
public class AccessIdentifier {

    @Id
    @GeneratedValue
    private UUID id;

    // Идентификатор всегда привязан к одному физическому лицу
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // Тип: card, pin, qr, biometric
    @Column(name = "identifier_type", nullable = false, length = 50)
    private String identifierType;

    // Хэш фактического значения идентификатора
    @Column(name = "identifier_value_hash", nullable = false, columnDefinition = "text")
    private String identifierValueHash;

    // Маскированное значение для отображения в интерфейсе
    @Column(name = "identifier_masked", length = 100)
    private String identifierMasked;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    // active, blocked, expired, deleted
    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Считыватель, с которого был получен идентификатор
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    // Пользователь системы, который выдал идентификатор
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by")
    private SystemUser issuedBy;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(columnDefinition = "text")
    private String comment;

    public AccessIdentifier() {
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        // При создании фиксируем даты
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = "active";
        }

        if (this.issuedAt == null) {
            this.issuedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // При обновлении меняем updated_at
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValueHash() {
        return identifierValueHash;
    }

    public void setIdentifierValueHash(String identifierValueHash) {
        this.identifierValueHash = identifierValueHash;
    }

    public String getIdentifierMasked() {
        return identifierMasked;
    }

    public void setIdentifierMasked(String identifierMasked) {
        this.identifierMasked = identifierMasked;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public SystemUser getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(SystemUser issuedBy) {
        this.issuedBy = issuedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}