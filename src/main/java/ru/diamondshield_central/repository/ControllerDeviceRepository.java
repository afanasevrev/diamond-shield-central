package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.ControllerDevice;

import java.util.UUID;

public interface ControllerDeviceRepository extends JpaRepository<ControllerDevice, UUID> {

    Page<ControllerDevice> findByObjectId(UUID objectId, Pageable pageable);

    Page<ControllerDevice> findByLocalServerId(UUID localServerId, Pageable pageable);

    boolean existsBySerialNumberIgnoreCase(String serialNumber);
}