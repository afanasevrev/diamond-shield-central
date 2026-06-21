package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.organization.*;
import ru.diamondshield_central.service.OrganizationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORGANIZATION_VIEW') or hasRole('SYSTEM_ADMIN')")
    public Page<OrganizationResponse> getAll(Pageable pageable) {
        return organizationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_VIEW') or hasRole('SYSTEM_ADMIN')")
    public OrganizationResponse getById(@PathVariable UUID id) {
        return organizationService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZATION_MANAGE') or hasRole('SYSTEM_ADMIN')")
    public OrganizationResponse create(@Valid @RequestBody OrganizationCreateRequest request,
                                       HttpServletRequest httpRequest) {
        return organizationService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_MANAGE') or hasRole('SYSTEM_ADMIN')")
    public OrganizationResponse update(@PathVariable UUID id,
                                       @Valid @RequestBody OrganizationUpdateRequest request,
                                       HttpServletRequest httpRequest) {
        return organizationService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZATION_MANAGE') or hasRole('SYSTEM_ADMIN')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        organizationService.deactivate(id, httpRequest);
    }
}