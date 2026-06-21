package ru.diamondshield_central.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class SystemUserUpdateRequest {

    private UUID organizationId;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 30)
    private String phone;

    @Size(max = 255)
    private String fullName;

    private Boolean active;

    public SystemUserUpdateRequest() {
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}