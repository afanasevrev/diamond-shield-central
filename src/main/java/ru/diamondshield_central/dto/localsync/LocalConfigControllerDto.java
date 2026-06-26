package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.ControllerDevice;

import java.util.UUID;

public class LocalConfigControllerDto {

    private UUID id;
    private String name;
    private String model;
    private String serialNumber;
    private String ipAddress;
    private Integer port;
    private String status;

    public static LocalConfigControllerDto fromEntity(ControllerDevice controller) {
        LocalConfigControllerDto dto = new LocalConfigControllerDto();

        // Локальному серверу нужны параметры подключения к контроллеру
        dto.id = controller.getId();
        dto.name = controller.getName();
        dto.model = controller.getModel();
        dto.serialNumber = controller.getSerialNumber();
        dto.ipAddress = controller.getIpAddress();
        dto.port = controller.getPort();
        dto.status = controller.getStatus();

        return dto;
    }

    public UUID getId() {
        return id;
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
}