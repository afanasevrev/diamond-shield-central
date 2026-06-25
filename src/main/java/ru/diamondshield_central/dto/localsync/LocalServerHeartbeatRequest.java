package ru.diamondshield_central.dto.localsync;

import jakarta.validation.constraints.Size;

public class LocalServerHeartbeatRequest {

    @Size(max = 50)
    private String ipAddress;

    @Size(max = 50)
    private String softwareVersion;

    @Size(max = 50)
    private String status;

    private String message;

    public LocalServerHeartbeatRequest() {
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}