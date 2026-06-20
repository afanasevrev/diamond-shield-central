package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.organization.*;
import ru.diamondshield_central.entity.Organization;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.OrganizationRepository;

import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final AuditService auditService;

    public OrganizationService(OrganizationRepository organizationRepository,
                               AuditService auditService) {
        this.organizationRepository = organizationRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAll(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(OrganizationResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getById(UUID id) {
        return OrganizationResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public OrganizationResponse create(OrganizationCreateRequest request, HttpServletRequest httpRequest) {
        if (organizationRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Organization with this name already exists");
        }

        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setInn(request.getInn());
        organization.setDescription(request.getDescription());
        organization.setActive(true);

        Organization saved = organizationRepository.save(organization);

        auditService.log("ORGANIZATION_CREATED", "organizations", saved.getId(), null, saved, httpRequest);

        return OrganizationResponse.fromEntity(saved);
    }

    @Transactional
    public OrganizationResponse update(UUID id,
                                       OrganizationUpdateRequest request,
                                       HttpServletRequest httpRequest) {
        Organization organization = findEntity(id);

        Organization oldValue = copy(organization);

        organization.setName(request.getName());
        organization.setInn(request.getInn());
        organization.setDescription(request.getDescription());

        if (request.getActive() != null) {
            organization.setActive(request.getActive());
        }

        Organization saved = organizationRepository.save(organization);

        auditService.log("ORGANIZATION_UPDATED", "organizations", saved.getId(), oldValue, saved, httpRequest);

        return OrganizationResponse.fromEntity(saved);
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        Organization organization = findEntity(id);
        Organization oldValue = copy(organization);

        organization.setActive(false);
        organizationRepository.save(organization);

        auditService.log("ORGANIZATION_DEACTIVATED", "organizations", id, oldValue, organization, httpRequest);
    }

    private Organization findEntity(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
    }

    private Organization copy(Organization source) {
        Organization copy = new Organization();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setInn(source.getInn());
        copy.setDescription(source.getDescription());
        copy.setActive(source.getActive());
        return copy;
    }
}
