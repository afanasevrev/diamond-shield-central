package ru.diamondshield_central.dto.organization;

import ru.diamondshield_central.entity.Organization;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrganizationResponse {

    private UUID id;
    private String name;
    private String inn;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrganizationResponse() {
    }

    public static OrganizationResponse fromEntity(Organization organization) {
        OrganizationResponse response = new OrganizationResponse();
        response.id = organization.getId();
        response.name = organization.getName();
        response.inn = organization.getInn();
        response.description = organization.getDescription();
        response.active = organization.getActive();
        response.createdAt = organization.getCreatedAt();
        response.updatedAt = organization.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInn() {
        return inn;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}