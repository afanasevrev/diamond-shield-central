package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.AccessIdentifier;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalConfigIdentifierDto {

    private UUID id;
    private UUID personId;
    private String identifierType;
    private String identifierValueHash;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String status;

    public static LocalConfigIdentifierDto fromEntity(AccessIdentifier identifier) {
        LocalConfigIdentifierDto dto = new LocalConfigIdentifierDto();

        // Локальному серверу нужен хэш для проверки идентификатора без хранения исходного значения
        dto.id = identifier.getId();
        dto.personId = identifier.getPerson().getId();
        dto.identifierType = identifier.getIdentifierType();
        dto.identifierValueHash = identifier.getIdentifierValueHash();
        dto.validFrom = identifier.getValidFrom();
        dto.validTo = identifier.getValidTo();
        dto.status = identifier.getStatus();

        return dto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public String getIdentifierValueHash() {
        return identifierValueHash;
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
}