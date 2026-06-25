package ru.diamondshield_central.dto.identifier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class IdentifierUniqueCheckRequest {

    @NotBlank
    @Size(max = 50)
    private String identifierType;

    @NotBlank
    @Size(max = 255)
    private String identifierValue;

    public IdentifierUniqueCheckRequest() {
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }
}