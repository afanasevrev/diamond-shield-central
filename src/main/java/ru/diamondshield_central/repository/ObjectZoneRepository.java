package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.ObjectZone;

import java.util.UUID;

public interface ObjectZoneRepository extends JpaRepository<ObjectZone, UUID> {

    Page<ObjectZone> findByObjectId(UUID objectId, Pageable pageable);

    boolean existsByObjectIdAndNameIgnoreCase(UUID objectId, String name);
}