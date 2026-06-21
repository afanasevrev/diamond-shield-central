package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.zone.ObjectZoneCreateRequest;
import ru.diamondshield_central.dto.zone.ObjectZoneResponse;
import ru.diamondshield_central.dto.zone.ObjectZoneUpdateRequest;
import ru.diamondshield_central.service.ObjectZoneService;

import java.util.UUID;

@RestController
@RequestMapping("/api/object-zones")
public class ObjectZoneController {

    private final ObjectZoneService objectZoneService;

    public ObjectZoneController(ObjectZoneService objectZoneService) {
        this.objectZoneService = objectZoneService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<ObjectZoneResponse> getAll(@RequestParam(required = false) UUID objectId,
                                           Pageable pageable) {
        return objectZoneService.getAll(objectId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public ObjectZoneResponse getById(@PathVariable UUID id) {
        return objectZoneService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ObjectZoneResponse create(@Valid @RequestBody ObjectZoneCreateRequest request,
                                     HttpServletRequest httpRequest) {
        return objectZoneService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ObjectZoneResponse update(@PathVariable UUID id,
                                     @Valid @RequestBody ObjectZoneUpdateRequest request,
                                     HttpServletRequest httpRequest) {
        return objectZoneService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void delete(@PathVariable UUID id,
                       HttpServletRequest httpRequest) {
        objectZoneService.delete(id, httpRequest);
    }
}