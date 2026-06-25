package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.alarmevent.AlarmEventCreateRequest;
import ru.diamondshield_central.dto.alarmevent.AlarmEventResponse;
import ru.diamondshield_central.service.AlarmEventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/alarm-events")
public class AlarmEventController {

    private final AlarmEventService alarmEventService;

    public AlarmEventController(AlarmEventService alarmEventService) {
        this.alarmEventService = alarmEventService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ALARM_EVENT_VIEW')")
    public Page<AlarmEventResponse> getAll(@RequestParam(required = false) UUID objectId,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false) String severity,
                                           Pageable pageable) {
        return alarmEventService.getAll(objectId, status, severity, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ALARM_EVENT_VIEW')")
    public AlarmEventResponse getById(@PathVariable UUID id) {
        return alarmEventService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ALARM_EVENT_MANAGE')")
    public AlarmEventResponse create(@Valid @RequestBody AlarmEventCreateRequest request,
                                     HttpServletRequest httpRequest) {
        return alarmEventService.create(request, httpRequest);
    }

    @PostMapping("/{id}/acknowledge")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ALARM_EVENT_MANAGE')")
    public AlarmEventResponse acknowledge(@PathVariable UUID id,
                                          HttpServletRequest httpRequest) {
        // Подтверждение означает, что оператор увидел тревогу
        return alarmEventService.acknowledge(id, httpRequest);
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ALARM_EVENT_MANAGE')")
    public AlarmEventResponse resolve(@PathVariable UUID id,
                                      HttpServletRequest httpRequest) {
        // Закрытие означает, что инцидент обработан
        return alarmEventService.resolve(id, httpRequest);
    }
}