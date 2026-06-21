package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.devicestatus.DeviceStatusCreateRequest;
import ru.diamondshield_central.dto.devicestatus.DeviceStatusHistoryResponse;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.DeviceStatusHistory;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.DeviceStatusHistoryRepository;

import java.util.UUID;

@Service
public class DeviceStatusHistoryService {

    private final DeviceStatusHistoryRepository deviceStatusHistoryRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AuditService auditService;

    public DeviceStatusHistoryService(DeviceStatusHistoryRepository deviceStatusHistoryRepository,
                                      AccessObjectRepository accessObjectRepository,
                                      AuditService auditService) {
        this.deviceStatusHistoryRepository = deviceStatusHistoryRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<DeviceStatusHistoryResponse> getAll(UUID objectId,
                                                    String deviceType,
                                                    UUID deviceId,
                                                    Pageable pageable) {
        if (deviceType != null && deviceId != null) {
            return deviceStatusHistoryRepository
                    .findByDeviceTypeAndDeviceId(deviceType, deviceId, pageable)
                    .map(DeviceStatusHistoryResponse::fromEntity);
        }

        if (objectId != null) {
            return deviceStatusHistoryRepository.findByObjectId(objectId, pageable)
                    .map(DeviceStatusHistoryResponse::fromEntity);
        }

        return deviceStatusHistoryRepository.findAll(pageable)
                .map(DeviceStatusHistoryResponse::fromEntity);
    }

    @Transactional
    public DeviceStatusHistoryResponse create(DeviceStatusCreateRequest request,
                                              HttpServletRequest httpRequest) {
        AccessObject object = null;

        if (request.getObjectId() != null) {
            object = accessObjectRepository.findById(request.getObjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Object not found"));
        }

        DeviceStatusHistory history = new DeviceStatusHistory();
        history.setObject(object);
        history.setDeviceType(request.getDeviceType());
        history.setDeviceId(request.getDeviceId());
        history.setStatus(request.getStatus());
        history.setMessage(request.getMessage());

        DeviceStatusHistory saved = deviceStatusHistoryRepository.save(history);

        auditService.log("DEVICE_STATUS_CREATED", "device_status_history", saved.getId(), null, saved, httpRequest);

        return DeviceStatusHistoryResponse.fromEntity(saved);
    }
}