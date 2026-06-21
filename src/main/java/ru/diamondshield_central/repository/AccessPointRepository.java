package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AccessPoint;

import java.util.UUID;

public interface AccessPointRepository extends JpaRepository<AccessPoint, UUID> {

    Page<AccessPoint> findByObjectId(UUID objectId, Pageable pageable);

    Page<AccessPoint> findByControllerId(UUID controllerId, Pageable pageable);
}