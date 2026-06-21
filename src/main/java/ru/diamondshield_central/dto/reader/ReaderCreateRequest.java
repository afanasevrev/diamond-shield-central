package ru.diamondshield_central.dto.reader;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ReaderCreateRequest {

    @NotNull
    private UUID controllerId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String readerType;

    @Size(max = 20)
    private String direction;

    public ReaderCreateRequest() {
    }

    public UUID getControllerId() {
        return controllerId;
    }

    public void setControllerId(UUID controllerId) {
        this.controllerId = controllerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}