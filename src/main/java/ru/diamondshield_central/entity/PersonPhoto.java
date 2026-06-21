package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "person_photos")
public class PersonPhoto {

    @Id
    @GeneratedValue
    private UUID id;

    // Фотография принадлежит конкретному физическому лицу
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "content_type", length = 100)
    private String contentType;

    // Размер файла в байтах
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    // BYTEA в PostgreSQL
    @Lob
    @Column(name = "photo_data", nullable = false)
    private byte[] photoData;

    // Признак основной фотографии
    @Column(name = "is_main", nullable = false)
    private Boolean main;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public PersonPhoto() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        // По умолчанию фотография не основная
        if (this.main == null) {
            this.main = false;
        }
    }

    public UUID getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}