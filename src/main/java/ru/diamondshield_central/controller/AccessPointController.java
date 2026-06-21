package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.accesspoint.AccessPointCreateRequest;
import ru.diamondshield_central.dto.accesspoint.AccessPointResponse;
import ru.diamondshield_central.dto.accesspoint.AccessPointUpdateRequest;
import ru.diamondshield_central.service.AccessPointService;

import java.util.UUID;

@RestController
@RequestMapping("/api/access-points")
public class AccessPointController {

    private final AccessPointService accessPointService;

    public AccessPointController(AccessPointService accessPointService) {
        this.accessPointService = accessPointService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<AccessPointResponse> getAll(@RequestParam(required = false) UUID objectId,
                                            @RequestParam(required = false) UUID controllerId,
                                            Pageable pageable) {
        return accessPointService.getAll(objectId, controllerId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public AccessPointResponse getById(@PathVariable UUID id) {
        return accessPointService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public AccessPointResponse create(@Valid @RequestBody AccessPointCreateRequest request,
                                      HttpServletRequest httpRequest) {
        return accessPointService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public AccessPointResponse update(@PathVariable UUID id,
                                      @Valid @RequestBody AccessPointUpdateRequest request,
                                      HttpServletRequest httpRequest) {
        return accessPointService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        accessPointService.deactivate(id, httpRequest);
    }
}