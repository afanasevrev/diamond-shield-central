package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.accessevent.AccessEventCreateRequest;
import ru.diamondshield_central.dto.accessevent.AccessEventResponse;
import ru.diamondshield_central.service.AccessEventService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/access-events")
public class AccessEventController {

    private final AccessEventService accessEventService;

    public AccessEventController(AccessEventService accessEventService) {
        this.accessEventService = accessEventService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_EVENT_VIEW')")
    public Page<AccessEventResponse> getAll(@RequestParam(required = false) UUID objectId,
                                            @RequestParam(required = false) UUID personId,
                                            @RequestParam(required = false) UUID accessPointId,
                                            @RequestParam(required = false) Boolean unknown,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                            LocalDateTime from,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                            LocalDateTime to,
                                            Pageable pageable) {
        return accessEventService.getAll(objectId, personId, accessPointId, unknown, from, to, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_EVENT_VIEW')")
    public AccessEventResponse getById(@PathVariable UUID id) {
        return accessEventService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_EVENT_VIEW')")
    public AccessEventResponse create(@Valid @RequestBody AccessEventCreateRequest request,
                                      HttpServletRequest httpRequest) {
        // На этапе 5 этот endpoint можно заменить или дополнить API для локальных серверов
        return accessEventService.create(request, httpRequest);
    }
}