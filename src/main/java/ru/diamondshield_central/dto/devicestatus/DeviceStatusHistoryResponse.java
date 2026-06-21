package ru.diamondshield_central.dto.devicestatus;

import ru.diamondshield_central.entity.DeviceStatusHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeviceStatusHistoryResponse {

    private UUID id;
    private UUID objectId;
    private String deviceType;
    private UUID deviceId;
    private String status;
    private String message;
    private LocalDateTime createdAt;

    public DeviceStatusHistoryResponse() {
    }

    public static DeviceStatusHistoryResponse fromEntity(DeviceStatusHistory history) {
        DeviceStatusHistoryResponse response = new DeviceStatusHistoryResponse();
        response.id = history.getId();

        if (history.getObject() != null) {
            response.objectId = history.getObject().getId();
        }

        response.deviceType = history.getDeviceType();
        response.deviceId = history.getDeviceId();
        response.status = history.getStatus();
        response.message = history.getMessage();
        response.createdAt = history.getCreatedAt();
        return response;
    }

    public UUID getId() {
        return id;
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