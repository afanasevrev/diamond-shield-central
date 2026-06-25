package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.SyncHistory;

import java.util.UUID;

public interface SyncHistoryRepository extends JpaRepository<SyncHistory, UUID> {

    // Получить историю синхронизации конкретного локального сервера
    Page<SyncHistory> findByLocalServerId(UUID localServerId, Pageable pageable);

    // Получить историю синхронизации по объекту
    Page<SyncHistory> findByObjectId(UUID objectId, Pageable pageable);

    // Фильтр по статусу
    Page<SyncHistory> findByStatus(String status, Pageable pageable);
}