package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AuditLog;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}