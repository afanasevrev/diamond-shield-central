package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.DeviceStatusHistory;

import java.util.UUID;

public interface DeviceStatusHistoryRepository extends JpaRepository<DeviceStatusHistory, UUID> {

    Page<DeviceStatusHistory> findByObjectId(UUID objectId, Pageable pageable);

    Page<DeviceStatusHistory> findByDeviceTypeAndDeviceId(String deviceType, UUID deviceId, Pageable pageable);
}