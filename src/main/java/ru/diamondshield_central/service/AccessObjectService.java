package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.object.AccessObjectCreateRequest;
import ru.diamondshield_central.dto.object.AccessObjectResponse;
import ru.diamondshield_central.dto.object.AccessObjectUpdateRequest;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.Organization;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.OrganizationRepository;

import java.util.UUID;

@Service
public class AccessObjectService {

    private final AccessObjectRepository accessObjectRepository;
    private final OrganizationRepository organizationRepository;
    private final AuditService auditService;

    public AccessObjectService(AccessObjectRepository accessObjectRepository,
                               OrganizationRepository organizationRepository,
                               AuditService auditService) {
        this.accessObjectRepository = accessObjectRepository;
        this.organizationRepository = organizationRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AccessObjectResponse> getAll(UUID organizationId, Pageable pageable) {
        if (organizationId != null) {
            return accessObjectRepository.findByOrganizationId(organizationId, pageable)
                    .map(AccessObjectResponse::fromEntity);
        }

        return accessObjectRepository.findAll(pageable)
                .map(AccessObjectResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AccessObjectResponse getById(UUID id) {
        return AccessObjectResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public AccessObjectResponse create(AccessObjectCreateRequest request, HttpServletRequest httpRequest) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        if (accessObjectRepository.existsByOrganizationIdAndNameIgnoreCase(
                request.getOrganizationId(),
                request.getName()
        )) {
            throw new ConflictException("Object with this name already exists in organization");
        }

        AccessObject object = new AccessObject();
        object.setOrganization(organization);
        object.setName(request.getName());
        object.setObjectType(request.getObjectType());
        object.setAddress(request.getAddress());
        object.setTimezone(request.getTimezone());
        object.setWorkScheduleId(request.getWorkScheduleId());
        object.setDescription(request.getDescription());
        object.setActive(true);

        AccessObject saved = accessObjectRepository.save(object);

        auditService.log("OBJECT_CREATED", "objects", saved.getId(), null, saved, httpRequest);

        return AccessObjectResponse.fromEntity(saved);
    }

    @Transactional
    public AccessObjectResponse update(UUID id,
                                       AccessObjectUpdateRequest request,
                                       HttpServletRequest httpRequest) {
        AccessObject object = findEntity(id);
        AccessObject oldValue = copy(object);

        object.setName(request.getName());
        object.setObjectType(request.getObjectType());
        object.setAddress(request.getAddress());
        object.setTimezone(request.getTimezone());
        object.setWorkScheduleId(request.getWorkScheduleId());
        object.setDescription(request.getDescription());

        if (request.getActive() != null) {
            object.setActive(request.getActive());
        }

        AccessObject saved = accessObjectRepository.save(object);

        auditService.log("OBJECT_UPDATED", "objects", saved.getId(), oldValue, saved, httpRequest);

        return AccessObjectResponse.fromEntity(saved);
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        AccessObject object = findEntity(id);
        AccessObject oldValue = copy(object);

        object.setActive(false);
        accessObjectRepository.save(object);

        auditService.log("OBJECT_DEACTIVATED", "objects", id, oldValue, object, httpRequest);
    }

    public AccessObject findEntity(UUID id) {
        return accessObjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));
    }

    private AccessObject copy(AccessObject source) {
        AccessObject copy = new AccessObject();
        copy.setId(source.getId());
        copy.setOrganization(source.getOrganization());
        copy.setName(source.getName());
        copy.setObjectType(source.getObjectType());
        copy.setAddress(source.getAddress());
        copy.setTimezone(source.getTimezone());
        copy.setWorkScheduleId(source.getWorkScheduleId());
        copy.setDescription(source.getDescription());
        copy.setActive(source.getActive());
        return copy;
    }
}