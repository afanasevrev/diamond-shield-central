package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.accesspoint.AccessPointCreateRequest;
import ru.diamondshield_central.dto.accesspoint.AccessPointResponse;
import ru.diamondshield_central.dto.accesspoint.AccessPointUpdateRequest;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.AccessPoint;
import ru.diamondshield_central.entity.ControllerDevice;
import ru.diamondshield_central.entity.ObjectZone;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.AccessPointRepository;
import ru.diamondshield_central.repository.ControllerDeviceRepository;
import ru.diamondshield_central.repository.ObjectZoneRepository;

import java.util.UUID;

@Service
public class AccessPointService {

    private final AccessPointRepository accessPointRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final ObjectZoneRepository objectZoneRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final AuditService auditService;

    public AccessPointService(AccessPointRepository accessPointRepository,
                              AccessObjectRepository accessObjectRepository,
                              ObjectZoneRepository objectZoneRepository,
                              ControllerDeviceRepository controllerDeviceRepository,
                              AuditService auditService) {
        this.accessPointRepository = accessPointRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.objectZoneRepository = objectZoneRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AccessPointResponse> getAll(UUID objectId,
                                            UUID controllerId,
                                            Pageable pageable) {
        if (objectId != null) {
            return accessPointRepository.findByObjectId(objectId, pageable)
                    .map(AccessPointResponse::fromEntity);
        }

        if (controllerId != null) {
            return accessPointRepository.findByControllerId(controllerId, pageable)
                    .map(AccessPointResponse::fromEntity);
        }

        return accessPointRepository.findAll(pageable)
                .map(AccessPointResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AccessPointResponse getById(UUID id) {
        return AccessPointResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public AccessPointResponse create(AccessPointCreateRequest request,
                                      HttpServletRequest httpRequest) {
        AccessObject object = accessObjectRepository.findById(request.getObjectId())
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));

        ObjectZone zoneFrom = findZoneOrNull(request.getZoneFromId());
        ObjectZone zoneTo = findZoneOrNull(request.getZoneToId());
        ControllerDevice controller = findControllerOrNull(request.getControllerId());

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setObject(object);
        accessPoint.setZoneFrom(zoneFrom);
        accessPoint.setZoneTo(zoneTo);
        accessPoint.setController(controller);
        accessPoint.setName(request.getName());
        accessPoint.setAccessPointType(request.getAccessPointType());
        accessPoint.setStatus("offline");
        accessPoint.setActive(true);

        AccessPoint saved = accessPointRepository.save(accessPoint);

        auditService.log("ACCESS_POINT_CREATED", "access_points", saved.getId(), null, saved, httpRequest);

        return AccessPointResponse.fromEntity(saved);
    }

    @Transactional
    public AccessPointResponse update(UUID id,
                                      AccessPointUpdateRequest request,
                                      HttpServletRequest httpRequest) {
        AccessPoint accessPoint = findEntity(id);
        AccessPoint oldValue = copy(accessPoint);

        accessPoint.setZoneFrom(findZoneOrNull(request.getZoneFromId()));
        accessPoint.setZoneTo(findZoneOrNull(request.getZoneToId()));
        accessPoint.setController(findControllerOrNull(request.getControllerId()));
        accessPoint.setName(request.getName());
        accessPoint.setAccessPointType(request.getAccessPointType());

        if (request.getStatus() != null) {
            accessPoint.setStatus(request.getStatus());
        }

        if (request.getActive() != null) {
            accessPoint.setActive(request.getActive());
        }

        AccessPoint saved = accessPointRepository.save(accessPoint);

        auditService.log("ACCESS_POINT_UPDATED", "access_points", saved.getId(), oldValue, saved, httpRequest);

        return AccessPointResponse.fromEntity(saved);
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        AccessPoint accessPoint = findEntity(id);
        AccessPoint oldValue = copy(accessPoint);

        accessPoint.setActive(false);
        accessPointRepository.save(accessPoint);

        auditService.log("ACCESS_POINT_DEACTIVATED", "access_points", id, oldValue, accessPoint, httpRequest);
    }

    public AccessPoint findEntity(UUID id) {
        return accessPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access point not found"));
    }

    private ObjectZone findZoneOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return objectZoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Object zone not found"));
    }

    private ControllerDevice findControllerOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return controllerDeviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Controller not found"));
    }

    private AccessPoint copy(AccessPoint source) {
        AccessPoint copy = new AccessPoint();
        copy.setObject(source.getObject());
        copy.setZoneFrom(source.getZoneFrom());
        copy.setZoneTo(source.getZoneTo());
        copy.setController(source.getController());
        copy.setName(source.getName());
        copy.setAccessPointType(source.getAccessPointType());
        copy.setStatus(source.getStatus());
        copy.setActive(source.getActive());
        return copy;
    }
}