package ru.diamondshield_central.dto.user;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignRoleRequest {

    @NotNull
    private UUID roleId;

    private UUID objectId;

    public AssignRoleRequest() {
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }
}