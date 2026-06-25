package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.schedule.ScheduleCreateRequest;
import ru.diamondshield_central.dto.schedule.ScheduleResponse;
import ru.diamondshield_central.dto.schedule.ScheduleUpdateRequest;
import ru.diamondshield_central.service.ScheduleService;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SCHEDULE_VIEW')")
    public Page<ScheduleResponse> getAll(@RequestParam(required = false) UUID organizationId,
                                         Pageable pageable) {
        return scheduleService.getAll(organizationId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SCHEDULE_VIEW')")
    public ScheduleResponse getById(@PathVariable UUID id) {
        return scheduleService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SCHEDULE_MANAGE')")
    public ScheduleResponse create(@Valid @RequestBody ScheduleCreateRequest request,
                                   HttpServletRequest httpRequest) {
        return scheduleService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SCHEDULE_MANAGE')")
    public ScheduleResponse update(@PathVariable UUID id,
                                   @Valid @RequestBody ScheduleUpdateRequest request,
                                   HttpServletRequest httpRequest) {
        return scheduleService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SCHEDULE_MANAGE')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        // Расписание деактивируется, но не удаляется из БД
        scheduleService.deactivate(id, httpRequest);
    }
}