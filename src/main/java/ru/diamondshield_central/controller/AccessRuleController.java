package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.accessrule.AccessRuleCreateRequest;
import ru.diamondshield_central.dto.accessrule.AccessRuleResponse;
import ru.diamondshield_central.dto.accessrule.AccessRuleUpdateRequest;
import ru.diamondshield_central.service.AccessRuleService;

import java.util.UUID;

@RestController
@RequestMapping("/api/access-rules")
public class AccessRuleController {

    private final AccessRuleService accessRuleService;

    public AccessRuleController(AccessRuleService accessRuleService) {
        this.accessRuleService = accessRuleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_RULE_VIEW')")
    public Page<AccessRuleResponse> getAll(@RequestParam(required = false) UUID personId,
                                           @RequestParam(required = false) UUID accessPointId,
                                           Pageable pageable) {
        return accessRuleService.getAll(personId, accessPointId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_RULE_VIEW')")
    public AccessRuleResponse getById(@PathVariable UUID id) {
        return accessRuleService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_RULE_MANAGE')")
    public AccessRuleResponse create(@Valid @RequestBody AccessRuleCreateRequest request,
                                     HttpServletRequest httpRequest) {
        return accessRuleService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_RULE_MANAGE')")
    public AccessRuleResponse update(@PathVariable UUID id,
                                     @Valid @RequestBody AccessRuleUpdateRequest request,
                                     HttpServletRequest httpRequest) {
        return accessRuleService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('ACCESS_RULE_MANAGE')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        // Правило доступа деактивируется логически
        accessRuleService.deactivate(id, httpRequest);
    }
}