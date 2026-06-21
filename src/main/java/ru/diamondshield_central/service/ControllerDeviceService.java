package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceCreateRequest;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceResponse;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceUpdateRequest;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.ControllerDevice;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.ControllerDeviceRepository;
import ru.diamondshield_central.repository.LocalServerRepository;

import java.util.UUID;

@Service
public class ControllerDeviceService {

    private final ControllerDeviceRepository controllerDeviceRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final LocalServerRepository localServerRepository;
    private final AuditService auditService;

    public ControllerDeviceService(ControllerDeviceRepository controllerDeviceRepository,
                                   AccessObjectRepository accessObjectRepository,
                                   LocalServerRepository localServerRepository,
                                   AuditService auditService) {
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.localServerRepository = localServerRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<ControllerDeviceResponse> getAll(UUID objectId,
                                                 UUID localServerId,
                                                 Pageable pageable) {
        if (objectId != null) {
            return controllerDeviceRepository.findByObjectId(objectId, pageable)
                    .map(ControllerDeviceResponse::fromEntity);
        }

        if (localServerId != null) {
            return controllerDeviceRepository.findByLocalServerId(localServerId, pageable)
                    .map(ControllerDeviceResponse::fromEntity);
        }

        return controllerDeviceRepository.findAll(pageable)
                .map(ControllerDeviceResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ControllerDeviceResponse getById(UUID id) {
        return ControllerDeviceResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public ControllerDeviceResponse create(ControllerDeviceCreateRequest request,
                                           HttpServletRequest httpRequest) {
        AccessObject object = accessObjectRepository.findById(request.getObjectId())
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));

        LocalServer localServer = null;
        if (request.getLocalServerId() != null) {
            localServer = localServerRepository.findById(request.getLocalServerId())
                    .orElseThrow(() -> new EntityNotFoundException("Local server not found"));
        }

        if (request.getSerialNumber() != null
                && !request.getSerialNumber().isBlank()
                && controllerDeviceRepository.existsBySerialNumberIgnoreCase(request.getSerialNumber())) {
            throw new ConflictException("Controller with this serial number already exists");
        }

        ControllerDevice controller = new ControllerDevice();
        controller.setObject(object);
        controller.setLocalServer(localServer);
        controller.setName(request.getName());
        controller.setModel(request.getModel());
        controller.setSerialNumber(request.getSerialNumber());
        controller.setIpAddress(request.getIpAddress());
        controller.setPort(request.getPort());
        controller.setStatus("offline");

        ControllerDevice saved = controllerDeviceRepository.save(controller);

        auditService.log("CONTROLLER_CREATED", "controllers", saved.getId(), null, saved, httpRequest);

        return ControllerDeviceResponse.fromEntity(saved);
    }

    @Transactional
    public ControllerDeviceResponse update(UUID id,
                                           ControllerDeviceUpdateRequest request,
                                           HttpServletRequest httpRequest) {
        ControllerDevice controller = findEntity(id);
        ControllerDevice oldValue = copy(controller);

        LocalServer localServer = null;
        if (request.getLocalServerId() != null) {
            localServer = localServerRepository.findById(request.getLocalServerId())
                    .orElseThrow(() -> new EntityNotFoundException("Local server not found"));
        }

        controller.setLocalServer(localServer);
        controller.setName(request.getName());
        controller.setModel(request.getModel());
        controller.setSerialNumber(request.getSerialNumber());
        controller.setIpAddress(request.getIpAddress());
        controller.setPort(request.getPort());

        if (request.getStatus() != null) {
            controller.setStatus(request.getStatus());
        }

        ControllerDevice saved = controllerDeviceRepository.save(controller);

        auditService.log("CONTROLLER_UPDATED", "controllers", saved.getId(), oldValue, saved, httpRequest);

        return ControllerDeviceResponse.fromEntity(saved);
    }

    @Transactional
    public void setOffline(UUID id, HttpServletRequest httpRequest) {
        ControllerDevice controller = findEntity(id);
        ControllerDevice oldValue = copy(controller);

        controller.setStatus("offline");
        controllerDeviceRepository.save(controller);

        auditService.log("CONTROLLER_OFFLINE", "controllers", id, oldValue, controller, httpRequest);
    }

    public ControllerDevice findEntity(UUID id) {
        return controllerDeviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Controller not found"));
    }

    private ControllerDevice copy(ControllerDevice source) {
        ControllerDevice copy = new ControllerDevice();
        copy.setObject(source.getObject());
        copy.setLocalServer(source.getLocalServer());
        copy.setName(source.getName());
        copy.setModel(source.getModel());
        copy.setSerialNumber(source.getSerialNumber());
        copy.setIpAddress(source.getIpAddress());
        copy.setPort(source.getPort());
        copy.setStatus(source.getStatus());
        copy.setLastSeenAt(source.getLastSeenAt());
        return copy;
    }
}