package ru.diamondshield_central.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.entity.ImportHistory;
import ru.diamondshield_central.entity.ImportHistoryDetail;
import ru.diamondshield_central.repository.ImportHistoryDetailRepository;
import ru.diamondshield_central.repository.ImportHistoryRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/imports/persons")
public class PersonImportHistoryController {

    private final ImportHistoryRepository historyRepository;
    private final ImportHistoryDetailRepository detailRepository;

    public PersonImportHistoryController(
            ImportHistoryRepository historyRepository,
            ImportHistoryDetailRepository detailRepository
    ) {
        this.historyRepository = historyRepository;
        this.detailRepository = detailRepository;
    }

    @GetMapping
    @PreAuthorize("""
        hasRole('SYSTEM_ADMIN')
        or hasAuthority('PERSON_IMPORT')
    """)
    public Page<ImportHistory> history(
            @RequestParam UUID organizationId,
            Pageable pageable
    ) {
        return historyRepository
                .findByOrganizationId(organizationId, pageable);
    }

    @GetMapping("/{id}/errors")
    @PreAuthorize("""
        hasRole('SYSTEM_ADMIN')
        or hasAuthority('PERSON_IMPORT')
    """)
    public List<ImportHistoryDetail> errors(
            @PathVariable UUID id
    ) {
        return detailRepository
                .findByImportHistoryIdOrderByRowNumberAsc(id);
    }
}