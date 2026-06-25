package ru.diamondshield_central.dto.identifier;

import ru.diamondshield_central.entity.AccessIdentifier;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessIdentifierResponse {

    private UUID id;
    private UUID personId;
    private String personFullName;
    private String identifierType;
    private String identifierMasked;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String status;
    private UUID readerId;
    private UUID issuedBy;
    private LocalDateTime issuedAt;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccessIdentifierResponse() {
    }

    public static AccessIdentifierResponse fromEntity(AccessIdentifier identifier) {
        AccessIdentifierResponse response = new AccessIdentifierResponse();

        // В ответ не включаем identifierValueHash
        response.id = identifier.getId();

        if (identifier.getPerson() != null) {
            response.personId = identifier.getPerson().getId();
            response.personFullName =
                    identifier.getPerson().getLastName() + " " +
                            identifier.getPerson().getFirstName() +
                            (identifier.getPerson().getMiddleName() == null ? "" : " " + identifier.getPerson().getMiddleName());
        }

        response.identifierType = identifier.getIdentifierType();
        response.identifierMasked = identifier.getIdentifierMasked();
        response.validFrom = identifier.getValidFrom();
        response.validTo = identifier.getValidTo();
        response.status = identifier.getStatus();

        if (identifier.getReader() != null) {
            response.readerId = identifier.getReader().getId();
        }

        if (identifier.getIssuedBy() != null) {
            response.issuedBy = identifier.getIssuedBy().getId();
        }

        response.issuedAt = identifier.getIssuedAt();
        response.comment = identifier.getComment();
        response.createdAt = identifier.getCreatedAt();
        response.updatedAt = identifier.getUpdatedAt();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public String getIdentifierMasked() {
        return identifierMasked;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public String getStatus() {
        return status;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public UUID getIssuedBy() {
        return issuedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}