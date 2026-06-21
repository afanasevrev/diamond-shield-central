package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.devicestatus.DeviceStatusCreateRequest;
import ru.diamondshield_central.dto.devicestatus.DeviceStatusHistoryResponse;
import ru.diamondshield_central.service.DeviceStatusHistoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/device-status-history")
public class DeviceStatusHistoryController {

    private final DeviceStatusHistoryService deviceStatusHistoryService;

    public DeviceStatusHistoryController(DeviceStatusHistoryService deviceStatusHistoryService) {
        this.deviceStatusHistoryService = deviceStatusHistoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<DeviceStatusHistoryResponse> getAll(@RequestParam(required = false) UUID objectId,
                                                    @RequestParam(required = false) String deviceType,
                                                    @RequestParam(required = false) UUID deviceId,
                                                    Pageable pageable) {
        return deviceStatusHistoryService.getAll(objectId, deviceType, deviceId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public DeviceStatusHistoryResponse create(@Valid @RequestBody DeviceStatusCreateRequest request,
                                              HttpServletRequest httpRequest) {
        return deviceStatusHistoryService.create(request, httpRequest);
    }
}