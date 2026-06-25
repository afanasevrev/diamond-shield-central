package ru.diamondshield_central.dto.photo;

import ru.diamondshield_central.entity.PersonPhoto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PersonPhotoResponse {

    private UUID id;
    private UUID personId;
    private String fileName;
    private String contentType;
    private Integer fileSize;
    private Boolean main;
    private LocalDateTime createdAt;

    public PersonPhotoResponse() {
    }

    public static PersonPhotoResponse fromEntity(PersonPhoto photo) {
        PersonPhotoResponse response = new PersonPhotoResponse();

        // В ответе отдаем только метаданные фотографии
        response.id = photo.getId();

        if (photo.getPerson() != null) {
            response.personId = photo.getPerson().getId();
        }

        response.fileName = photo.getFileName();
        response.contentType = photo.getContentType();
        response.fileSize = photo.getFileSize();
        response.main = photo.getMain();
        response.createdAt = photo.getCreatedAt();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public Boolean getMain() {
        return main;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}