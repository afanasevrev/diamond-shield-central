package ru.diamondshield_central.dto.websocket;

import java.time.LocalDateTime;
import java.util.UUID;

public class CardBindingNotification {

    private String eventType;
    private UUID readerId;
    private UUID personId;
    private LocalDateTime createdAt;

    public CardBindingNotification() {
    }

    public CardBindingNotification(String eventType,
                                   UUID readerId,
                                   UUID personId,
                                   LocalDateTime createdAt) {
        // eventType: wait_started, wait_cancelled, card_received
        this.eventType = eventType;
        this.readerId = readerId;
        this.personId = personId;
        this.createdAt = createdAt;
    }

    public String getEventType() {
        return eventType;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}