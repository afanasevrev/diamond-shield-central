package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.object.AccessObjectCreateRequest;
import ru.diamondshield_central.dto.object.AccessObjectResponse;
import ru.diamondshield_central.dto.object.AccessObjectUpdateRequest;
import ru.diamondshield_central.service.AccessObjectService;

import java.util.UUID;

@RestController
@RequestMapping("/api/objects")
public class AccessObjectController {

    private final AccessObjectService accessObjectService;

    public AccessObjectController(AccessObjectService accessObjectService) {
        this.accessObjectService = accessObjectService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<AccessObjectResponse> getAll(@RequestParam(required = false) UUID organizationId,
                                             Pageable pageable) {
        return accessObjectService.getAll(organizationId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public AccessObjectResponse getById(@PathVariable UUID id) {
        return accessObjectService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public AccessObjectResponse create(@Valid @RequestBody AccessObjectCreateRequest request,
                                       HttpServletRequest httpRequest) {
        return accessObjectService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public AccessObjectResponse update(@PathVariable UUID id,
                                       @Valid @RequestBody AccessObjectUpdateRequest request,
                                       HttpServletRequest httpRequest) {
        return accessObjectService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        accessObjectService.deactivate(id, httpRequest);
    }
}