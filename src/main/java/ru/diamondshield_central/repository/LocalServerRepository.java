package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.LocalServer;

import java.util.Optional;
import java.util.UUID;

public interface LocalServerRepository extends JpaRepository<LocalServer, UUID> {

    Page<LocalServer> findByObjectId(UUID objectId, Pageable pageable);

    Optional<LocalServer> findByMacAddressIgnoreCase(String macAddress);
}