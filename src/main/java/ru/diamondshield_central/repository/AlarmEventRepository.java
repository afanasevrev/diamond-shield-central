package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AlarmEvent;

import java.util.UUID;

public interface AlarmEventRepository extends JpaRepository<AlarmEvent, UUID> {

    Page<AlarmEvent> findByObjectId(UUID objectId, Pageable pageable);

    Page<AlarmEvent> findByStatus(String status, Pageable pageable);

    Page<AlarmEvent> findBySeverity(String severity, Pageable pageable);

    boolean existsByLocalServerIdAndLocalEventId(UUID localServerId, String localEventId);
}