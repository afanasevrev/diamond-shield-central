package ru.diamondshield_central.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    public RoleUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}