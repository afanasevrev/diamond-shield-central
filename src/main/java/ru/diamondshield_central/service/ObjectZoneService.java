package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.zone.ObjectZoneCreateRequest;
import ru.diamondshield_central.dto.zone.ObjectZoneResponse;
import ru.diamondshield_central.dto.zone.ObjectZoneUpdateRequest;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.ObjectZone;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.ObjectZoneRepository;

import java.util.UUID;

@Service
public class ObjectZoneService {

    private final ObjectZoneRepository objectZoneRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AuditService auditService;

    public ObjectZoneService(ObjectZoneRepository objectZoneRepository,
                             AccessObjectRepository accessObjectRepository,
                             AuditService auditService) {
        this.objectZoneRepository = objectZoneRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<ObjectZoneResponse> getAll(UUID objectId, Pageable pageable) {
        if (objectId != null) {
            return objectZoneRepository.findByObjectId(objectId, pageable)
                    .map(ObjectZoneResponse::fromEntity);
        }

        return objectZoneRepository.findAll(pageable)
                .map(ObjectZoneResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ObjectZoneResponse getById(UUID id) {
        return ObjectZoneResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public ObjectZoneResponse create(ObjectZoneCreateRequest request, HttpServletRequest httpRequest) {
        AccessObject object = accessObjectRepository.findById(request.getObjectId())
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));

        if (objectZoneRepository.existsByObjectIdAndNameIgnoreCase(
                request.getObjectId(),
                request.getName()
        )) {
            throw new ConflictException("Zone with this name already exists in object");
        }

        ObjectZone zone = new ObjectZone();
        zone.setObject(object);
        zone.setName(request.getName());
        zone.setZoneType(request.getZoneType());
        zone.setDescription(request.getDescription());

        ObjectZone saved = objectZoneRepository.save(zone);

        auditService.log("OBJECT_ZONE_CREATED", "object_zones", saved.getId(), null, saved, httpRequest);

        return ObjectZoneResponse.fromEntity(saved);
    }

    @Transactional
    public ObjectZoneResponse update(UUID id,
                                     ObjectZoneUpdateRequest request,
                                     HttpServletRequest httpRequest) {
        ObjectZone zone = findEntity(id);
        ObjectZone oldValue = copy(zone);

        zone.setName(request.getName());
        zone.setZoneType(request.getZoneType());
        zone.setDescription(request.getDescription());

        ObjectZone saved = objectZoneRepository.save(zone);

        auditService.log("OBJECT_ZONE_UPDATED", "object_zones", saved.getId(), oldValue, saved, httpRequest);

        return ObjectZoneResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(UUID id, HttpServletRequest httpRequest) {
        ObjectZone zone = findEntity(id);
        ObjectZone oldValue = copy(zone);

        objectZoneRepository.delete(zone);

        auditService.log("OBJECT_ZONE_DELETED", "object_zones", id, oldValue, null, httpRequest);
    }

    public ObjectZone findEntity(UUID id) {
        return objectZoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Object zone not found"));
    }

    private ObjectZone copy(ObjectZone source) {
        ObjectZone copy = new ObjectZone();
        copy.setObject(source.getObject());
        copy.setName(source.getName());
        copy.setZoneType(source.getZoneType());
        copy.setDescription(source.getDescription());
        return copy;
    }
}