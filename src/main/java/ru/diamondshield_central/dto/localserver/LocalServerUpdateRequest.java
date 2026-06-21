package ru.diamondshield_central.dto.localserver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocalServerUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String ipAddress;

    @Size(max = 50)
    private String macAddress;

    private String serverTokenHash;

    @Size(max = 50)
    private String softwareVersion;

    @Size(max = 50)
    private String status;

    public LocalServerUpdateRequest() {
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

    public String getServerTokenHash() {
        return serverTokenHash;
    }

    public void setServerTokenHash(String serverTokenHash) {
        this.serverTokenHash = serverTokenHash;
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
}