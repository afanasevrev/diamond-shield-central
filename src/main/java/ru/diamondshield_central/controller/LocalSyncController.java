package ru.diamondshield_central.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.localsync.*;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.service.LocalConfigService;
import ru.diamondshield_central.service.LocalServerAuthService;
import ru.diamondshield_central.service.LocalServerHeartbeatService;
import ru.diamondshield_central.service.LocalSyncService;
import ru.diamondshield_central.service.SyncHistoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/local-sync")
public class LocalSyncController {

    private final LocalServerAuthService localServerAuthService;
    private final LocalServerHeartbeatService heartbeatService;
    private final LocalConfigService localConfigService;
    private final LocalSyncService localSyncService;
    private final SyncHistoryService syncHistoryService;

    public LocalSyncController(LocalServerAuthService localServerAuthService,
                               LocalServerHeartbeatService heartbeatService,
                               LocalConfigService localConfigService,
                               LocalSyncService localSyncService,
                               SyncHistoryService syncHistoryService) {
        this.localServerAuthService = localServerAuthService;
        this.heartbeatService = heartbeatService;
        this.localConfigService = localConfigService;
        this.localSyncService = localSyncService;
        this.syncHistoryService = syncHistoryService;
    }

    @PostMapping("/heartbeat")
    public LocalServerHeartbeatResponse heartbeat(@RequestHeader("X-Local-Server-Id") UUID localServerId,
                                                  @RequestHeader("X-Local-Server-Token") String token,
                                                  @Valid @RequestBody LocalServerHeartbeatRequest request) {
        // Проверяем локальный сервер по отдельному токену
        LocalServer localServer = localServerAuthService.authenticate(localServerId, token);

        return heartbeatService.heartbeat(localServer, request);
    }

    @GetMapping("/config")
    public LocalConfigResponse getConfig(@RequestHeader("X-Local-Server-Id") UUID localServerId,
                                         @RequestHeader("X-Local-Server-Token") String token) {
        LocalServer localServer = localServerAuthService.authenticate(localServerId, token);

        LocalConfigResponse response = localConfigService.buildConfig(localServer);

        // Фиксируем выдачу конфигурации как синхронизацию
        syncHistoryService.finish(
                syncHistoryService.start(localServer, "config_pull"),
                1,
                1,
                0,
                0,
                null
        );

        localConfigService.markConfigPulled(localServer);

        return response;
    }

    @PostMapping("/access-events")
    public LocalBatchPushResponse pushAccessEvents(@RequestHeader("X-Local-Server-Id") UUID localServerId,
                                                   @RequestHeader("X-Local-Server-Token") String token,
                                                   @Valid @RequestBody List<LocalAccessEventPushRequest> events) {
        LocalServer localServer = localServerAuthService.authenticate(localServerId, token);

        return localSyncService.pushAccessEvents(localServer, events);
    }

    @PostMapping("/alarm-events")
    public LocalBatchPushResponse pushAlarmEvents(@RequestHeader("X-Local-Server-Id") UUID localServerId,
                                                  @RequestHeader("X-Local-Server-Token") String token,
                                                  @Valid @RequestBody List<LocalAlarmEventPushRequest> events) {
        LocalServer localServer = localServerAuthService.authenticate(localServerId, token);

        return localSyncService.pushAlarmEvents(localServer, events);
    }

    @PostMapping("/device-statuses")
    public LocalBatchPushResponse pushDeviceStatuses(@RequestHeader("X-Local-Server-Id") UUID localServerId,
                                                     @RequestHeader("X-Local-Server-Token") String token,
                                                     @Valid @RequestBody List<LocalDeviceStatusPushRequest> statuses) {
        LocalServer localServer = localServerAuthService.authenticate(localServerId, token);

        return localSyncService.pushDeviceStatuses(localServer, statuses);
    }
}