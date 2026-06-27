package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.accesscheck.AccessCheckRequest;
import ru.diamondshield_central.dto.accesscheck.AccessCheckResponse;
import ru.diamondshield_central.dto.accessevent.AccessEventResponse;
import ru.diamondshield_central.entity.*;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessCheckService {

    private final AccessIdentifierRepository accessIdentifierRepository;
    private final AccessRuleRepository accessRuleRepository;
    private final AccessEventRepository accessEventRepository;
    private final AccessPointRepository accessPointRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final ReaderRepository readerRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final IdentifierSecurityService identifierSecurityService;
    private final ScheduleCheckService scheduleCheckService;
    private final WebSocketNotificationService webSocketNotificationService;

    public AccessCheckService(AccessIdentifierRepository accessIdentifierRepository,
                              AccessRuleRepository accessRuleRepository,
                              AccessEventRepository accessEventRepository,
                              AccessPointRepository accessPointRepository,
                              AccessObjectRepository accessObjectRepository,
                              ReaderRepository readerRepository,
                              ControllerDeviceRepository controllerDeviceRepository,
                              IdentifierSecurityService identifierSecurityService,
                              ScheduleCheckService scheduleCheckService,
                              WebSocketNotificationService webSocketNotificationService) {
        this.accessIdentifierRepository = accessIdentifierRepository;
        this.accessRuleRepository = accessRuleRepository;
        this.accessEventRepository = accessEventRepository;
        this.accessPointRepository = accessPointRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.readerRepository = readerRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.identifierSecurityService = identifierSecurityService;
        this.scheduleCheckService = scheduleCheckService;
        this.webSocketNotificationService = webSocketNotificationService;
    }

    @Transactional
    public AccessCheckResponse check(AccessCheckRequest request) {
        LocalDateTime checkedAt = request.getEventTime() == null
                ? LocalDateTime.now()
                : request.getEventTime();

        AccessPoint accessPoint = accessPointRepository.findById(request.getAccessPointId())
                .orElseThrow(() -> new EntityNotFoundException("Access point not found"));

        AccessObject object = findObject(request.getObjectId(), accessPoint);
        Reader reader = findReaderOrNull(request.getReaderId());
        ControllerDevice controller = findControllerOrNull(request.getControllerId());

        String normalizedValue = identifierSecurityService.normalize(request.getIdentifierValue());
        String identifierHash = identifierSecurityService.sha256(normalizedValue);
        String identifierMasked = identifierSecurityService.mask(normalizedValue);

        Optional<AccessIdentifier> identifierOptional =
                accessIdentifierRepository.findByIdentifierTypeAndIdentifierValueHash(
                        request.getIdentifierType(),
                        identifierHash
                );

        if (identifierOptional.isEmpty()) {
            AccessEvent event = saveEvent(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    null,
                    null,
                    checkedAt,
                    request.getDirection(),
                    "unknown",
                    "Unknown identifier",
                    request.getIdentifierType(),
                    identifierMasked,
                    identifierHash,
                    true
            );

            return response(
                    "unknown_identifier",
                    false,
                    "Identifier was not found",
                    null,
                    null,
                    null,
                    identifierMasked,
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        AccessIdentifier identifier = identifierOptional.get();
        Person person = identifier.getPerson();

        if (person == null || !Boolean.TRUE.equals(person.getActive())) {
            AccessEvent event = saveEvent(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "denied",
                    "Person is inactive",
                    identifier.getIdentifierType(),
                    identifier.getIdentifierMasked(),
                    identifier.getIdentifierValueHash(),
                    false
            );

            return response(
                    "inactive_person",
                    false,
                    "Person is inactive",
                    personId(person),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        if ("blocked".equalsIgnoreCase(identifier.getStatus())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Identifier is blocked"
            );

            return response(
                    "blocked_identifier",
                    false,
                    "Identifier is blocked",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        if ("deleted".equalsIgnoreCase(identifier.getStatus())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Identifier is deleted"
            );

            return response(
                    "deleted_identifier",
                    false,
                    "Identifier is deleted",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        if (!"active".equalsIgnoreCase(identifier.getStatus())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Identifier is not active"
            );

            return response(
                    "denied",
                    false,
                    "Identifier is not active",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        if (identifier.getValidFrom() != null && checkedAt.isBefore(identifier.getValidFrom())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Identifier validity period has not started"
            );

            return response(
                    "expired_identifier",
                    false,
                    "Identifier validity period has not started",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        if (identifier.getValidTo() != null && checkedAt.isAfter(identifier.getValidTo())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Identifier validity period has expired"
            );

            return response(
                    "expired_identifier",
                    false,
                    "Identifier validity period has expired",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        Optional<AccessRule> ruleOptional =
                accessRuleRepository.findByPersonIdAndAccessPointId(person.getId(), accessPoint.getId());

        if (ruleOptional.isEmpty()) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "No access rule"
            );

            return response(
                    "no_rule",
                    false,
                    "No access rule for this person and access point",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    null,
                    null,
                    event.getId(),
                    checkedAt
            );
        }

        AccessRule rule = ruleOptional.get();

        if (!Boolean.TRUE.equals(rule.getActive())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Access rule is inactive"
            );

            return response(
                    "inactive_rule",
                    false,
                    "Access rule is inactive",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    rule.getId(),
                    scheduleId(rule),
                    event.getId(),
                    checkedAt
            );
        }

        if (rule.getValidFrom() != null && checkedAt.isBefore(rule.getValidFrom())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Access rule validity period has not started"
            );

            return response(
                    "rule_not_started",
                    false,
                    "Access rule validity period has not started",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    rule.getId(),
                    scheduleId(rule),
                    event.getId(),
                    checkedAt
            );
        }

        if (rule.getValidTo() != null && checkedAt.isAfter(rule.getValidTo())) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Access rule validity period has expired"
            );

            return response(
                    "rule_expired",
                    false,
                    "Access rule validity period has expired",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    rule.getId(),
                    scheduleId(rule),
                    event.getId(),
                    checkedAt
            );
        }

        boolean allowedBySchedule = scheduleCheckService.isAllowedBySchedule(rule.getSchedule(), checkedAt);

        if (!allowedBySchedule) {
            AccessEvent event = saveDeniedKnownIdentifier(
                    object,
                    accessPoint,
                    reader,
                    controller,
                    person,
                    identifier,
                    checkedAt,
                    request.getDirection(),
                    "Out of schedule"
            );

            return response(
                    "out_of_schedule",
                    false,
                    "Access denied by schedule",
                    person.getId(),
                    fullName(person),
                    identifier.getId(),
                    identifier.getIdentifierMasked(),
                    accessPoint.getId(),
                    rule.getId(),
                    scheduleId(rule),
                    event.getId(),
                    checkedAt
            );
        }

        AccessEvent allowedEvent = saveEvent(
                object,
                accessPoint,
                reader,
                controller,
                person,
                identifier,
                checkedAt,
                request.getDirection(),
                "allowed",
                "Access granted",
                identifier.getIdentifierType(),
                identifier.getIdentifierMasked(),
                identifier.getIdentifierValueHash(),
                false
        );

        return response(
                "allowed",
                true,
                "Access granted",
                person.getId(),
                fullName(person),
                identifier.getId(),
                identifier.getIdentifierMasked(),
                accessPoint.getId(),
                rule.getId(),
                scheduleId(rule),
                allowedEvent.getId(),
                checkedAt
        );
    }

    private AccessEvent saveDeniedKnownIdentifier(AccessObject object,
                                                  AccessPoint accessPoint,
                                                  Reader reader,
                                                  ControllerDevice controller,
                                                  Person person,
                                                  AccessIdentifier identifier,
                                                  LocalDateTime eventTime,
                                                  String direction,
                                                  String reason) {
        return saveEvent(
                object,
                accessPoint,
                reader,
                controller,
                person,
                identifier,
                eventTime,
                direction,
                "denied",
                reason,
                identifier.getIdentifierType(),
                identifier.getIdentifierMasked(),
                identifier.getIdentifierValueHash(),
                false
        );
    }

    private AccessEvent saveEvent(AccessObject object,
                                  AccessPoint accessPoint,
                                  Reader reader,
                                  ControllerDevice controller,
                                  Person person,
                                  AccessIdentifier identifier,
                                  LocalDateTime eventTime,
                                  String direction,
                                  String accessResult,
                                  String reason,
                                  String identifierType,
                                  String identifierMasked,
                                  String identifierValueHash,
                                  Boolean unknownIdentifier) {
        AccessEvent event = new AccessEvent();

        // Это событие создано центральным сервером, поэтому localServer/localEventId не заполняем
        event.setObject(object);
        event.setAccessPoint(accessPoint);
        event.setReader(reader);
        event.setController(controller);
        event.setPerson(person);
        event.setIdentifier(identifier);
        event.setEventTime(eventTime);
        event.setDirection(direction);
        event.setAccessResult(accessResult);
        event.setReason(reason);
        event.setIdentifierType(identifierType);
        event.setIdentifierMasked(identifierMasked);
        event.setIdentifierValueHash(identifierValueHash);
        event.setUnknownIdentifier(Boolean.TRUE.equals(unknownIdentifier));

        AccessEvent saved = accessEventRepository.save(event);

        // Все проверки доступа сразу отправляем в WebSocket-журнал
        webSocketNotificationService.sendAccessEvent(AccessEventResponse.fromEntity(saved));

        return saved;
    }

    private AccessCheckResponse response(String decision,
                                         Boolean allowed,
                                         String reason,
                                         UUID personId,
                                         String personFullName,
                                         UUID identifierId,
                                         String identifierMasked,
                                         UUID accessPointId,
                                         UUID accessRuleId,
                                         UUID scheduleId,
                                         UUID accessEventId,
                                         LocalDateTime checkedAt) {
        return AccessCheckResponse.of(
                decision,
                allowed,
                reason,
                personId,
                personFullName,
                identifierId,
                identifierMasked,
                accessPointId,
                accessRuleId,
                scheduleId,
                accessEventId,
                checkedAt
        );
    }

    private AccessObject findObject(UUID requestedObjectId, AccessPoint accessPoint) {
        if (requestedObjectId != null) {
            return accessObjectRepository.findById(requestedObjectId)
                    .orElseThrow(() -> new EntityNotFoundException("Object not found"));
        }

        if (accessPoint.getObject() != null) {
            return accessPoint.getObject();
        }

        return null;
    }

    private Reader findReaderOrNull(UUID readerId) {
        if (readerId == null) {
            return null;
        }

        return readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
    }

    private ControllerDevice findControllerOrNull(UUID controllerId) {
        if (controllerId == null) {
            return null;
        }

        return controllerDeviceRepository.findById(controllerId)
                .orElseThrow(() -> new EntityNotFoundException("Controller not found"));
    }

    private UUID personId(Person person) {
        return person == null ? null : person.getId();
    }

    private String fullName(Person person) {
        if (person == null) {
            return null;
        }

        String result = person.getLastName() + " " + person.getFirstName();

        if (person.getMiddleName() != null && !person.getMiddleName().isBlank()) {
            result += " " + person.getMiddleName();
        }

        return result;
    }

    private UUID scheduleId(AccessRule rule) {
        if (rule == null || rule.getSchedule() == null) {
            return null;
        }

        return rule.getSchedule().getId();
    }
}