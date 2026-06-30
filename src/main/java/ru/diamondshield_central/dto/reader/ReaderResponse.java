package ru.diamondshield_central.dto.reader;

import ru.diamondshield_central.entity.Reader;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReaderResponse {

    private UUID id;
    private UUID controllerId;
    private String name;
    private String readerType;
    private String direction;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID accessPointId;
    private Integer percoExdevNumber;
    private Integer percoDirection;

    public ReaderResponse() {
    }

    public static ReaderResponse fromEntity(Reader reader) {
        ReaderResponse response = new ReaderResponse();
        response.id = reader.getId();
        response.controllerId = reader.getController().getId();
        response.name = reader.getName();
        response.readerType = reader.getReaderType();
        response.direction = reader.getDirection();
        response.status = reader.getStatus();
        response.createdAt = reader.getCreatedAt();
        response.updatedAt = reader.getUpdatedAt();
        return response;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(UUID accessPointId) {
        this.accessPointId = accessPointId;
    }

    public Integer getPercoExdevNumber() {
        return percoExdevNumber;
    }

    public void setPercoExdevNumber(Integer percoExdevNumber) {
        this.percoExdevNumber = percoExdevNumber;
    }

    public Integer getPercoDirection() {
        return percoDirection;
    }

    public void setPercoDirection(Integer percoDirection) {
        this.percoDirection = percoDirection;
    }
}