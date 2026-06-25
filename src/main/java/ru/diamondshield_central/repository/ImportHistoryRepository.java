package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.ImportHistory;

import java.util.UUID;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, UUID> {

    // Получить историю импортов конкретной организации
    Page<ImportHistory> findByOrganizationId(UUID organizationId, Pageable pageable);
}