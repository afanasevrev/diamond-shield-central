package ru.diamondshield_central.dto.localserver;

import ru.diamondshield_central.entity.LocalServer;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalServerResponse {

    private UUID id;
    private UUID objectId;
    private String name;
    private String ipAddress;
    private String macAddress;
    private String softwareVersion;
    private String status;
    private LocalDateTime lastSeenAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LocalServerResponse() {
    }

    public static LocalServerResponse fromEntity(LocalServer localServer) {
        LocalServerResponse response = new LocalServerResponse();
        response.id = localServer.getId();
        response.objectId = localServer.getObject().getId();
        response.name = localServer.getName();
        response.ipAddress = localServer.getIpAddress();
        response.macAddress = localServer.getMacAddress();
        response.softwareVersion = localServer.getSoftwareVersion();
        response.status = localServer.getStatus();
        response.lastSeenAt = localServer.getLastSeenAt();
        response.createdAt = localServer.getCreatedAt();
        response.updatedAt = localServer.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
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