package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.accessevent.AccessEventCreateRequest;
import ru.diamondshield_central.dto.accessevent.AccessEventResponse;
import ru.diamondshield_central.entity.*;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessEventService {

    private final AccessEventRepository accessEventRepository;
    private final LocalServerRepository localServerRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AccessPointRepository accessPointRepository;
    private final ReaderRepository readerRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final PersonRepository personRepository;
    private final AccessIdentifierRepository accessIdentifierRepository;
    private final IdentifierSecurityService identifierSecurityService;
    private final AuditService auditService;

    public AccessEventService(AccessEventRepository accessEventRepository,
                              LocalServerRepository localServerRepository,
                              AccessObjectRepository accessObjectRepository,
                              AccessPointRepository accessPointRepository,
                              ReaderRepository readerRepository,
                              ControllerDeviceRepository controllerDeviceRepository,
                              PersonRepository personRepository,
                              AccessIdentifierRepository accessIdentifierRepository,
                              IdentifierSecurityService identifierSecurityService,
                              AuditService auditService) {
        this.accessEventRepository = accessEventRepository;
        this.localServerRepository = localServerRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.accessPointRepository = accessPointRepository;
        this.readerRepository = readerRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.personRepository = personRepository;
        this.accessIdentifierRepository = accessIdentifierRepository;
        this.identifierSecurityService = identifierSecurityService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AccessEventResponse> getAll(UUID objectId,
                                            UUID personId,
                                            UUID accessPointId,
                                            Boolean unknown,
                                            LocalDateTime from,
                                            LocalDateTime to,
                                            Pageable pageable) {
        if (Boolean.TRUE.equals(unknown)) {
            return accessEventRepository.findByUnknownIdentifierTrue(pageable)
                    .map(AccessEventResponse::fromEntity);
        }

        if (objectId != null) {
            return accessEventRepository.findByObjectId(objectId, pageable)
                    .map(AccessEventResponse::fromEntity);
        }

        if (personId != null) {
            return accessEventRepository.findByPersonId(personId, pageable)
                    .map(AccessEventResponse::fromEntity);
        }

        if (accessPointId != null) {
            return accessEventRepository.findByAccessPointId(accessPointId, pageable)
                    .map(AccessEventResponse::fromEntity);
        }

        if (from != null && to != null) {
            return accessEventRepository.findByEventTimeBetween(from, to, pageable)
                    .map(AccessEventResponse::fromEntity);
        }

        return accessEventRepository.findAll(pageable)
                .map(AccessEventResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AccessEventResponse getById(UUID id) {
        return AccessEventResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public AccessEventResponse create(AccessEventCreateRequest request, HttpServletRequest httpRequest) {
        LocalServer localServer = findLocalServerOrNull(request.getLocalServerId());

        if (localServer != null
                && request.getLocalEventId() != null
                && accessEventRepository.existsByLocalServerIdAndLocalEventId(localServer.getId(), request.getLocalEventId())) {
            throw new ConflictException("Access event already exists");
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
            String hash = identifierSecurityService.sha256(normalized);

            event.setIdentifierValueHash(hash);
            event.setIdentifierMasked(identifierSecurityService.mask(normalized));

            // Если identifierId и personId не переданы, считаем идентификатор неизвестным
            if (request.getIdentifierId() == null && request.getPersonId() == null) {
                event.setUnknownIdentifier(true);
            }
        }

        if ("unknown".equalsIgnoreCase(request.getAccessResult())) {
            event.setUnknownIdentifier(true);
        }

        AccessEvent saved = accessEventRepository.save(event);

        auditService.log("ACCESS_EVENT_CREATED", "access_events", saved.getId(), null, AccessEventResponse.fromEntity(saved), httpRequest);

        return AccessEventResponse.fromEntity(saved);
    }

    public AccessEvent findEntity(UUID id) {
        return accessEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access event not found"));
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

    private Person findPersonOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }

    private AccessIdentifier findIdentifierOrNull(UUID id) {
        if (id == null) {
            return null;
        }

        return accessIdentifierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Identifier not found"));
    }
}