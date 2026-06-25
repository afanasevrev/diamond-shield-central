package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.identifier.AccessIdentifierCreateRequest;
import ru.diamondshield_central.dto.identifier.AccessIdentifierResponse;
import ru.diamondshield_central.dto.identifier.IdentifierUniqueCheckRequest;
import ru.diamondshield_central.dto.identifier.IdentifierUniqueCheckResponse;
import ru.diamondshield_central.service.AccessIdentifierService;

import java.util.UUID;

@RestController
@RequestMapping("/api/access-identifiers")
public class AccessIdentifierController {

    private final AccessIdentifierService accessIdentifierService;

    public AccessIdentifierController(AccessIdentifierService accessIdentifierService) {
        this.accessIdentifierService = accessIdentifierService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_VIEW')")
    public Page<AccessIdentifierResponse> getAll(@RequestParam(required = false) UUID personId,
                                                 Pageable pageable) {
        return accessIdentifierService.getAll(personId, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_VIEW')")
    public AccessIdentifierResponse getById(@PathVariable UUID id) {
        return accessIdentifierService.getById(id);
    }

    @PostMapping("/check-unique")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_VIEW')")
    public IdentifierUniqueCheckResponse checkUnique(@Valid @RequestBody IdentifierUniqueCheckRequest request) {
        // Метод используется интерфейсом перед подтверждением привязки карты
        return accessIdentifierService.checkUnique(
                request.getIdentifierType(),
                request.getIdentifierValue()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_MANAGE')")
    public AccessIdentifierResponse create(@Valid @RequestBody AccessIdentifierCreateRequest request,
                                           HttpServletRequest httpRequest) {
        return accessIdentifierService.create(request, httpRequest);
    }

    @PostMapping("/{id}/block")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_MANAGE')")
    public AccessIdentifierResponse block(@PathVariable UUID id,
                                          HttpServletRequest httpRequest) {
        return accessIdentifierService.block(id, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_MANAGE')")
    public AccessIdentifierResponse deactivate(@PathVariable UUID id,
                                               HttpServletRequest httpRequest) {
        // Удаление логическое: status=deleted
        return accessIdentifierService.deactivate(id, httpRequest);
    }
}