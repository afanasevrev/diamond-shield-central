package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.localserver.LocalServerCreateRequest;
import ru.diamondshield_central.dto.localserver.LocalServerResponse;
import ru.diamondshield_central.dto.localserver.LocalServerUpdateRequest;
import ru.diamondshield_central.service.LocalServerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/local-servers")
public class LocalServerController {

    private final LocalServerService localServerService;

    public LocalServerController(LocalServerService localServerService) {
        this.localServerService = localServerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<LocalServerResponse> getAll(@RequestParam(required = false) UUID objectId,
                                            Pageable pageable) {
        return localServerService.getAll(objectId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public LocalServerResponse getById(@PathVariable UUID id) {
        return localServerService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public LocalServerResponse create(@Valid @RequestBody LocalServerCreateRequest request,
                                      HttpServletRequest httpRequest) {
        return localServerService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public LocalServerResponse update(@PathVariable UUID id,
                                      @Valid @RequestBody LocalServerUpdateRequest request,
                                      HttpServletRequest httpRequest) {
        return localServerService.update(id, request, httpRequest);
    }

    @PostMapping("/{id}/seen")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public LocalServerResponse markSeen(@PathVariable UUID id,
                                        HttpServletRequest httpRequest) {
        return localServerService.markSeen(id, httpRequest);
    }

    @PostMapping("/{id}/offline")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void setOffline(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        localServerService.setOffline(id, httpRequest);
    }
}