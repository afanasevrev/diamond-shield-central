package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.ImportHistoryDetail;

import java.util.List;
import java.util.UUID;

public interface ImportHistoryDetailRepository extends JpaRepository<ImportHistoryDetail, UUID> {

    // Получить все детали конкретного импорта
    List<ImportHistoryDetail> findByImportHistoryIdOrderByRowNumberAsc(UUID importHistoryId);
}