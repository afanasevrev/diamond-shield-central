package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AccessObject;

import java.util.UUID;

public interface AccessObjectRepository extends JpaRepository<AccessObject, UUID> {

    Page<AccessObject> findByOrganizationId(UUID organizationId, Pageable pageable);

    boolean existsByOrganizationIdAndNameIgnoreCase(UUID organizationId, String name);
}