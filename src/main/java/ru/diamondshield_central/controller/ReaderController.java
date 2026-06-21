package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.reader.ReaderCreateRequest;
import ru.diamondshield_central.dto.reader.ReaderResponse;
import ru.diamondshield_central.dto.reader.ReaderUpdateRequest;
import ru.diamondshield_central.service.ReaderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public Page<ReaderResponse> getAll(@RequestParam(required = false) UUID controllerId,
                                       Pageable pageable) {
        return readerService.getAll(controllerId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_VIEW')")
    public ReaderResponse getById(@PathVariable UUID id) {
        return readerService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ReaderResponse create(@Valid @RequestBody ReaderCreateRequest request,
                                 HttpServletRequest httpRequest) {
        return readerService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public ReaderResponse update(@PathVariable UUID id,
                                 @Valid @RequestBody ReaderUpdateRequest request,
                                 HttpServletRequest httpRequest) {
        return readerService.update(id, request, httpRequest);
    }

    @PostMapping("/{id}/offline")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ORGANIZATION_MANAGE')")
    public void setOffline(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        readerService.setOffline(id, httpRequest);
    }
}