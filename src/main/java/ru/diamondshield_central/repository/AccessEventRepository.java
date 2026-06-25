package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AccessEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccessEventRepository extends JpaRepository<AccessEvent, UUID> {

    Page<AccessEvent> findByObjectId(UUID objectId, Pageable pageable);

    Page<AccessEvent> findByPersonId(UUID personId, Pageable pageable);

    Page<AccessEvent> findByAccessPointId(UUID accessPointId, Pageable pageable);

    Page<AccessEvent> findByUnknownIdentifierTrue(Pageable pageable);

    Page<AccessEvent> findByEventTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    boolean existsByLocalServerIdAndLocalEventId(UUID localServerId, String localEventId);
}