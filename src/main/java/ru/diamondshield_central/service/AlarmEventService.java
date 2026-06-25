package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.alarmevent.AlarmEventCreateRequest;
import ru.diamondshield_central.dto.alarmevent.AlarmEventResponse;
import ru.diamondshield_central.entity.*;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.*;
import ru.diamondshield_central.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AlarmEventService {

    private final AlarmEventRepository alarmEventRepository;
    private final LocalServerRepository localServerRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AccessPointRepository accessPointRepository;
    private final ReaderRepository readerRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final AuditService auditService;

    public AlarmEventService(AlarmEventRepository alarmEventRepository,
                             LocalServerRepository localServerRepository,
                             AccessObjectRepository accessObjectRepository,
                             AccessPointRepository accessPointRepository,
                             ReaderRepository readerRepository,
                             ControllerDeviceRepository controllerDeviceRepository,
                             AuditService auditService) {
        this.alarmEventRepository = alarmEventRepository;
        this.localServerRepository = localServerRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.accessPointRepository = accessPointRepository;
        this.readerRepository = readerRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AlarmEventResponse> getAll(UUID objectId,
                                           String status,
                                           String severity,
                                           Pageable pageable) {
        if (objectId != null) {
            return alarmEventRepository.findByObjectId(objectId, pageable)
                    .map(AlarmEventResponse::fromEntity);
        }

        if (status != null) {
            return alarmEventRepository.findByStatus(status, pageable)
                    .map(AlarmEventResponse::fromEntity);
        }

        if (severity != null) {
            return alarmEventRepository.findBySeverity(severity, pageable)
                    .map(AlarmEventResponse::fromEntity);
        }

        return alarmEventRepository.findAll(pageable)
                .map(AlarmEventResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AlarmEventResponse getById(UUID id) {
        return AlarmEventResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public AlarmEventResponse create(AlarmEventCreateRequest request, HttpServletRequest httpRequest) {
        LocalServer localServer = findLocalServerOrNull(request.getLocalServerId());

        if (localServer != null
                && request.getLocalEventId() != null
                && alarmEventRepository.existsByLocalServerIdAndLocalEventId(localServer.getId(), request.getLocalEventId())) {
            throw new ConflictException("Alarm event already exists");
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

        auditService.log("ALARM_EVENT_CREATED", "alarm_events", saved.getId(), null, AlarmEventResponse.fromEntity(saved), httpRequest);

        return AlarmEventResponse.fromEntity(saved);
    }

    @Transactional
    public AlarmEventResponse acknowledge(UUID id, HttpServletRequest httpRequest) {
        AlarmEvent event = findEntity(id);
        AlarmEventResponse oldValue = AlarmEventResponse.fromEntity(event);

        event.setStatus("acknowledged");
        event.setAcknowledgedAt(LocalDateTime.now());
        event.setAcknowledgedBy(getCurrentUserOrNull());

        AlarmEvent saved = alarmEventRepository.save(event);

        auditService.log("ALARM_EVENT_ACKNOWLEDGED", "alarm_events", saved.getId(), oldValue, AlarmEventResponse.fromEntity(saved), httpRequest);

        return AlarmEventResponse.fromEntity(saved);
    }

    @Transactional
    public AlarmEventResponse resolve(UUID id, HttpServletRequest httpRequest) {
        AlarmEvent event = findEntity(id);
        AlarmEventResponse oldValue = AlarmEventResponse.fromEntity(event);

        event.setStatus("resolved");
        event.setResolvedAt(LocalDateTime.now());
        event.setResolvedBy(getCurrentUserOrNull());

        AlarmEvent saved = alarmEventRepository.save(event);

        auditService.log("ALARM_EVENT_RESOLVED", "alarm_events", saved.getId(), oldValue, AlarmEventResponse.fromEntity(saved), httpRequest);

        return AlarmEventResponse.fromEntity(saved);
    }

    public AlarmEvent findEntity(UUID id) {
        return alarmEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alarm event not found"));
    }

    private SystemUser getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    private LocalServer findLocalServerOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return localServerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Local server not found"));
    }

    private AccessObject findObjectOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return accessObjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));
    }

    private AccessPoint findAccessPointOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return accessPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access point not found"));
    }

    private Reader findReaderOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return readerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
    }

    private ControllerDevice findControllerOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return controllerDeviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Controller not found"));
    }
}