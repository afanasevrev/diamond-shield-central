package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.Reader;

import java.util.UUID;

public class LocalConfigReaderDto {

    private UUID id;
    private UUID controllerId;
    private UUID accessPointId;
    private String name;
    private String readerType;
    private String direction;
    private String status;

    private Integer percoExdevNumber;

    private Integer percoDirection;

    public static LocalConfigReaderDto fromEntity(Reader reader) {
        LocalConfigReaderDto dto = new LocalConfigReaderDto();

        // Считыватель привязан к контроллеру
        dto.id = reader.getId();
        dto.controllerId = reader.getController().getId();
        dto.name = reader.getName();
        dto.readerType = reader.getReaderType();
        dto.direction = reader.getDirection();
        dto.status = reader.getStatus();
        dto.accessPointId= reader.getAccessPointId();
        dto.percoExdevNumber = reader.getPercoExdevNumber();
        dto.percoDirection = reader.getPercoDirection();
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public String getName() {
        return name;
    }

    public String getReaderType() {
        return readerType;
    }

    public String getDirection() {
        return direction;
    }

    public String getStatus() {
        return status;
    }
}