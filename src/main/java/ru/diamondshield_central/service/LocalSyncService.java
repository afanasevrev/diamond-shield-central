package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.accessevent.AccessEventResponse;
import ru.diamondshield_central.dto.alarmevent.AlarmEventResponse;
import ru.diamondshield_central.dto.localsync.*;
import ru.diamondshield_central.dto.websocket.DeviceStatusNotification;
import ru.diamondshield_central.entity.*;
import ru.diamondshield_central.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocalSyncService {

    private final AccessEventRepository accessEventRepository;
    private final AlarmEventRepository alarmEventRepository;
    private final DeviceStatusHistoryRepository deviceStatusHistoryRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AccessPointRepository accessPointRepository;
    private final ReaderRepository readerRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final PersonRepository personRepository;
    private final AccessIdentifierRepository accessIdentifierRepository;
    private final LocalServerRepository localServerRepository;
    private final IdentifierSecurityService identifierSecurityService;
    private final SyncHistoryService syncHistoryService;
    private final WebSocketNotificationService webSocketNotificationService;

    public LocalSyncService(AccessEventRepository accessEventRepository,
                            AlarmEventRepository alarmEventRepository,
                            DeviceStatusHistoryRepository deviceStatusHistoryRepository,
                            AccessObjectRepository accessObjectRepository,
                            AccessPointRepository accessPointRepository,
                            ReaderRepository readerRepository,
                            ControllerDeviceRepository controllerDeviceRepository,
                            PersonRepository personRepository,
                            AccessIdentifierRepository accessIdentifierRepository,
                            LocalServerRepository localServerRepository,
                            IdentifierSecurityService identifierSecurityService,
                            SyncHistoryService syncHistoryService,
                            WebSocketNotificationService webSocketNotificationService) {
        this.accessEventRepository = accessEventRepository;
        this.alarmEventRepository = alarmEventRepository;
        this.deviceStatusHistoryRepository = deviceStatusHistoryRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.accessPointRepository = accessPointRepository;
        this.readerRepository = readerRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.personRepository = personRepository;
        this.accessIdentifierRepository = accessIdentifierRepository;
        this.localServerRepository = localServerRepository;
        this.identifierSecurityService = identifierSecurityService;
        this.syncHistoryService = syncHistoryService;
        this.webSocketNotificationService = webSocketNotificationService;
    }

    @Transactional
    public LocalBatchPushResponse pushAccessEvents(LocalServer localServer,
                                                   List<LocalAccessEventPushRequest> events) {
        SyncHistory history = syncHistoryService.start(localServer, "access_events_push");

        int total = events == null ? 0 : events.size();
        int accepted = 0;
        int skipped = 0;
        int errors = 0;

        if (events != null) {
            for (LocalAccessEventPushRequest request : events) {
                try {
                    if (accessEventRepository.existsByLocalServerIdAndLocalEventId(localServer.getId(), request.getLocalEventId())) {
                        // Дубликат не является ошибкой, просто пропускаем
                        skipped++;
                        continue;
                    }

                    AccessEvent event = new AccessEvent();
                    event.setLocalServer(localServer);
                    event.setLocalEventId(request.getLocalEventId());
                    event.setObject(findObjectOrNull(request.getObjectId()));
                    event.setAccessPoint(findAccessPointOrNull(request.getAccessPointId()));
                    event.setReader(findReaderOrNull(request.getReaderId()));
                    event.setController(findControllerOrNull(request.getControllerId()));
                    event.setPerson(findPersonOrNull(request.getPersonId()));
                    event.setIdentifier(findIdentifierOrNull(request.getIdentifierId()));
                    event.setEventTime(request.getEventTime());
                    event.setDirection(request.getDirection());
                    event.setAccessResult(request.getAccessResult());
                    event.setReason(request.getReason());
                    event.setIdentifierType(request.getIdentifierType());

                    if (request.getIdentifierValue() != null && !request.getIdentifierValue().isBlank()) {
                        String normalized = identifierSecurityService.normalize(request.getIdentifierValue());
                        event.setIdentifierValueHash(identifierSecurityService.sha256(normalized));
                        event.setIdentifierMasked(identifierSecurityService.mask(normalized));
                    }

                    if ("unknown".equalsIgnoreCase(request.getAccessResult())
                            || (request.getPersonId() == null && request.getIdentifierId() == null)) {
                        event.setUnknownIdentifier(true);
                    }

                    AccessEvent saved = accessEventRepository.save(event);

                    // После сохранения отправляем событие в WebSocket
                    webSocketNotificationService.sendAccessEvent(AccessEventResponse.fromEntity(saved));

                    accepted++;
                } catch (Exception ex) {
                    errors++;
                }
            }
        }

        finishLocalServerSync(localServer);

        SyncHistory finished = syncHistoryService.finish(history, total, accepted, skipped, errors, null);

        return buildBatchResponse(finished);
    }

    @Transactional
    public LocalBatchPushResponse pushAlarmEvents(LocalServer localServer,
                                                  List<LocalAlarmEventPushRequest> events) {
        SyncHistory history = syncHistoryService.start(localServer, "alarm_events_push");

        int total = events == null ? 0 : events.size();
        int accepted = 0;
        int skipped = 0;
        int errors = 0;

        if (events != null) {
            for (LocalAlarmEventPushRequest request : events) {
                try {
                    if (alarmEventRepository.existsByLocalServerIdAndLocalEventId(localServer.getId(), request.getLocalEventId())) {
                        skipped++;
                        continue;
                    }

                    AlarmEvent event = new AlarmEvent();
                    event.setLocalServer(localServer);
                    event.setLocalEventId(request.getLocalEventId());
                    event.setObject(findObjectOrNull(request.getObjectId()));
                    event.setAccessPoint(findAccessPointOrNull(request.getAccessPointId()));
                    event.setReader(findReaderOrNull(request.getReaderId()));
                    event.setController(findControllerOrNull(request.getControllerId()));
                    event.setAlarmType(request.getAlarmType());
                    event.setSeverity(request.getSeverity());
                    event.setMessage(request.getMessage());
                    event.setOccurredAt(request.getOccurredAt());
                    event.setStatus("new");

                    AlarmEvent saved = alarmEventRepository.save(event);

                    // Новая тревога сразу уходит в интерфейс оператора
                    webSocketNotificationService.sendAlarmEvent(AlarmEventResponse.fromEntity(saved));

                    accepted++;
                } catch (Exception ex) {
                    errors++;
                }
            }
        }

        finishLocalServerSync(localServer);

        SyncHistory finished = syncHistoryService.finish(history, total, accepted, skipped, errors, null);

        return buildBatchResponse(finished);
    }

    @Transactional
    public LocalBatchPushResponse pushDeviceStatuses(LocalServer localServer,
                                                     List<LocalDeviceStatusPushRequest> statuses) {
        SyncHistory history = syncHistoryService.start(localServer, "device_status_push");

        int total = statuses == null ? 0 : statuses.size();
        int accepted = 0;
        int skipped = 0;
        int errors = 0;

        if (statuses != null) {
            for (LocalDeviceStatusPushRequest request : statuses) {
                try {
                    AccessObject object = findObjectOrNull(request.getObjectId());

                    DeviceStatusHistory statusHistory = new DeviceStatusHistory();
                    statusHistory.setObject(object);
                    statusHistory.setDeviceType(request.getDeviceType());
                    statusHistory.setDeviceId(request.getDeviceId());
                    statusHistory.setStatus(request.getStatus());
                    statusHistory.setMessage(request.getMessage());

                    DeviceStatusHistory saved = deviceStatusHistoryRepository.save(statusHistory);

                    // Обновляем текущий статус оборудования в основной таблице
                    updateDeviceCurrentStatus(request);

                    webSocketNotificationService.sendDeviceStatus(
                            new DeviceStatusNotification(
                                    request.getObjectId(),
                                    request.getDeviceType(),
                                    request.getDeviceId(),
                                    request.getStatus(),
                                    request.getMessage(),
                                    saved.getCreatedAt()
                            )
                    );

                    accepted++;
                } catch (Exception ex) {
                    errors++;
                }
            }
        }

        finishLocalServerSync(localServer);

        SyncHistory finished = syncHistoryService.finish(history, total, accepted, skipped, errors, null);

        return buildBatchResponse(finished);
    }

    private void updateDeviceCurrentStatus(LocalDeviceStatusPushRequest request) {
        String type = request.getDeviceType();

        if ("local_server".equalsIgnoreCase(type)) {
            // Статус локального сервера обновляется отдельно
            return;
        }

        if ("controller".equalsIgnoreCase(type)) {
            controllerDeviceRepository.findById(request.getDeviceId()).ifPresent(device -> {
                device.setStatus(request.getStatus());
                device.setLastSeenAt(LocalDateTime.now());
                controllerDeviceRepository.save(device);
            });
        }

        if ("reader".equalsIgnoreCase(type)) {
            readerRepository.findById(request.getDeviceId()).ifPresent(reader -> {
                reader.setStatus(request.getStatus());
                readerRepository.save(reader);
            });
        }

        if ("access_point".equalsIgnoreCase(type)) {
            accessPointRepository.findById(request.getDeviceId()).ifPresent(point -> {
                point.setStatus(request.getStatus());
                accessPointRepository.save(point);
            });
        }
    }

    private void finishLocalServerSync(LocalServer localServer) {
        // Обновляем last_sync_at у локального сервера
        localServer.setLastSyncAt(LocalDateTime.now());
        localServer.setLastSeenAt(LocalDateTime.now());
        localServer.setStatus("online");
        localServerRepository.save(localServer);
    }

    private LocalBatchPushResponse buildBatchResponse(SyncHistory history) {
        LocalBatchPushResponse response = new LocalBatchPushResponse();

        response.setSyncHistoryId(history.getId());
        response.setStatus(history.getStatus());
        response.setTotalItems(history.getTotalItems());
        response.setAcceptedItems(history.getAcceptedItems());
        response.setSkippedItems(history.getSkippedItems());
        response.setErrorItems(history.getErrorItems());

        return response;
    }

    private AccessObject findObjectOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return accessObjectRepository.findById(id).orElse(null);
    }

    private AccessPoint findAccessPointOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return accessPointRepository.findById(id).orElse(null);
    }

    private Reader findReaderOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return readerRepository.findById(id).orElse(null);
    }

    private ControllerDevice findControllerOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return controllerDeviceRepository.findById(id).orElse(null);
    }

    private Person findPersonOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return personRepository.findById(id).orElse(null);
    }

    private AccessIdentifier findIdentifierOrNull(java.util.UUID id) {
        if (id == null) {
            return null;
        }

        return accessIdentifierRepository.findById(id).orElse(null);
    }
}