package ru.diamondshield_central.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.sync.SyncHistoryResponse;
import ru.diamondshield_central.service.SyncHistoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/sync-history")
public class SyncHistoryController {

    private final SyncHistoryService syncHistoryService;

    public SyncHistoryController(SyncHistoryService syncHistoryService) {
        this.syncHistoryService = syncHistoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('SYNC_VIEW')")
    public Page<SyncHistoryResponse> getAll(@RequestParam(required = false) UUID localServerId,
                                            @RequestParam(required = false) UUID objectId,
                                            @RequestParam(required = false) String status,
                                            Pageable pageable) {
        // История нужна администраторам для диагностики проблем синхронизации
        return syncHistoryService.getAll(localServerId, objectId, status, pageable);
    }
}