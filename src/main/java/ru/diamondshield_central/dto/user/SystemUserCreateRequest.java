package ru.diamondshield_central.dto.user;

import jakarta.validation.constraints.*;
import java.util.UUID;

public class SystemUserCreateRequest {

    private UUID organizationId;

    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 30)
    private String phone;

    @Size(max = 255)
    private String fullName;

    public SystemUserCreateRequest() {
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}