package ru.diamondshield_central.dto.controllerdevice;

import ru.diamondshield_central.entity.ControllerDevice;

import java.time.LocalDateTime;
import java.util.UUID;

public class ControllerDeviceResponse {

    private UUID id;
    private UUID objectId;
    private UUID localServerId;
    private String name;
    private String model;
    private String serialNumber;
    private String ipAddress;
    private Integer port;
    private String status;
    private LocalDateTime lastSeenAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ControllerDeviceResponse() {
    }

    public static ControllerDeviceResponse fromEntity(ControllerDevice controller) {
        ControllerDeviceResponse response = new ControllerDeviceResponse();
        response.id = controller.getId();
        response.objectId = controller.getObject().getId();

        if (controller.getLocalServer() != null) {
            response.localServerId = controller.getLocalServer().getId();
        }

        response.name = controller.getName();
        response.model = controller.getModel();
        response.serialNumber = controller.getSerialNumber();
        response.ipAddress = controller.getIpAddress();
        response.port = controller.getPort();
        response.status = controller.getStatus();
        response.lastSeenAt = controller.getLastSeenAt();
        response.createdAt = controller.getCreatedAt();
        response.updatedAt = controller.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}