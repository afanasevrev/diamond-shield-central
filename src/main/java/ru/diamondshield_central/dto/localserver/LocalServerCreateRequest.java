package ru.diamondshield_central.dto.localserver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class LocalServerCreateRequest {

    @NotNull
    private UUID objectId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String ipAddress;

    @Size(max = 50)
    private String macAddress;

    @NotBlank
    @Size(min = 16, max = 255)
    private String serverToken;

    @Size(max = 50)
    private String softwareVersion;

    public LocalServerCreateRequest() {
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }
}