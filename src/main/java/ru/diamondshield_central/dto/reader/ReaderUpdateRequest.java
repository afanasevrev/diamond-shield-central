package ru.diamondshield_central.dto.reader;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReaderUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String readerType;

    @Size(max = 20)
    private String direction;

    @Size(max = 50)
    private String status;

    public ReaderUpdateRequest() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}