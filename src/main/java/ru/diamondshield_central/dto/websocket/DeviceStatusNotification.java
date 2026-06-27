package ru.diamondshield_central.dto.websocket;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeviceStatusNotification {

    private UUID objectId;
    private String deviceType;
    private UUID deviceId;
    private String status;
    private String message;
    private LocalDateTime createdAt;

    public DeviceStatusNotification() {
    }

    public DeviceStatusNotification(UUID objectId,
                                    String deviceType,
                                    UUID deviceId,
                                    String status,
                                    String message,
                                    LocalDateTime createdAt) {
        // DTO отправляется в WebSocket при изменении статуса оборудования
        this.objectId = objectId;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}