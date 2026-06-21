package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceCreateRequest;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceResponse;
import ru.diamondshield_central.dto.controllerdevice.ControllerDeviceUpdateRequest;
import ru.diamondshield_central.service.ControllerDeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/controllers")
public class ControllerDeviceController {

    private final ControllerDeviceService controllerDeviceService;

    public ControllerDeviceController(ControllerDeviceService controllerDeviceService) {
        this.controllerDeviceService = controllerDeviceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<ControllerDeviceResponse> getAll(@RequestParam(required = false) UUID objectId,
                                                 @RequestParam(required = false) UUID localServerId,
                                                 Pageable pageable) {
        return controllerDeviceService.getAll(objectId, localServerId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public ControllerDeviceResponse getById(@PathVariable UUID id) {
        return controllerDeviceService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ControllerDeviceResponse create(@Valid @RequestBody ControllerDeviceCreateRequest request,
                                           HttpServletRequest httpRequest) {
        return controllerDeviceService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ControllerDeviceResponse update(@PathVariable UUID id,
                                           @Valid @RequestBody ControllerDeviceUpdateRequest request,
                                           HttpServletRequest httpRequest) {
        return controllerDeviceService.update(id, request, httpRequest);
    }

    @PostMapping("/{id}/offline")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void setOffline(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        controllerDeviceService.setOffline(id, httpRequest);
    }
}