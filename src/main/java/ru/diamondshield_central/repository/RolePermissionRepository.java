package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.RolePermission;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    List<RolePermission> findByRoleId(UUID roleId);

    boolean existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);
}