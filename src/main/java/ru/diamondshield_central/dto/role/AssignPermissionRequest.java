package ru.diamondshield_central.dto.role;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignPermissionRequest {

    @NotNull
    private UUID permissionId;

    public AssignPermissionRequest() {
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }
}