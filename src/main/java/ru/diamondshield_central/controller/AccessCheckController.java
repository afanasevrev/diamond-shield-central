package ru.diamondshield_central.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.accesscheck.AccessCheckRequest;
import ru.diamondshield_central.dto.accesscheck.AccessCheckResponse;
import ru.diamondshield_central.service.AccessCheckService;

@RestController
@RequestMapping("/api/access-check")
public class AccessCheckController {

    private final AccessCheckService accessCheckService;

    public AccessCheckController(AccessCheckService accessCheckService) {
        this.accessCheckService = accessCheckService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_CHECK')")
    public AccessCheckResponse check(@Valid @RequestBody AccessCheckRequest request) {
        // Центральная проверка доступа по идентификатору и точке прохода
        return accessCheckService.check(request);
    }
}