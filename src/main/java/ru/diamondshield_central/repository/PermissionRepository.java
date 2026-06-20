package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.Permission;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByCode(String code);

    boolean existsByCodeIgnoreCase(String code);
}