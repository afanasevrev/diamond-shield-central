package ru.diamondshield_central.dto.cardbinding;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CardBindingWaitRequest {

    @NotNull
    private UUID readerId;

    @NotNull
    private UUID personId;

    public CardBindingWaitRequest() {
    }

    public UUID getReaderId() {
        return readerId;
    }

    public void setReaderId(UUID readerId) {
        this.readerId = readerId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }
}