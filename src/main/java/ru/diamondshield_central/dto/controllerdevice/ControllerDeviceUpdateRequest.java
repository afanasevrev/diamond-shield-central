package ru.diamondshield_central.dto.controllerdevice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ControllerDeviceUpdateRequest {

    private UUID localServerId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String model;

    @Size(max = 100)
    private String serialNumber;

    @Size(max = 50)
    private String ipAddress;

    private Integer port;

    @Size(max = 50)
    private String status;

    public ControllerDeviceUpdateRequest() {
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public void setLocalServerId(UUID localServerId) {
        this.localServerId = localServerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}