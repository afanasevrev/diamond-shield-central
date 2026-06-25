package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.accessrule.AccessRuleCreateRequest;
import ru.diamondshield_central.dto.accessrule.AccessRuleResponse;
import ru.diamondshield_central.dto.accessrule.AccessRuleUpdateRequest;
import ru.diamondshield_central.entity.AccessPoint;
import ru.diamondshield_central.entity.AccessRule;
import ru.diamondshield_central.entity.Person;
import ru.diamondshield_central.entity.Schedule;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessPointRepository;
import ru.diamondshield_central.repository.AccessRuleRepository;
import ru.diamondshield_central.repository.PersonRepository;
import ru.diamondshield_central.repository.ScheduleRepository;

import java.util.UUID;

@Service
public class AccessRuleService {

    private final AccessRuleRepository accessRuleRepository;
    private final PersonRepository personRepository;
    private final AccessPointRepository accessPointRepository;
    private final ScheduleRepository scheduleRepository;
    private final AuditService auditService;

    public AccessRuleService(AccessRuleRepository accessRuleRepository,
                             PersonRepository personRepository,
                             AccessPointRepository accessPointRepository,
                             ScheduleRepository scheduleRepository,
                             AuditService auditService) {
        this.accessRuleRepository = accessRuleRepository;
        this.personRepository = personRepository;
        this.accessPointRepository = accessPointRepository;
        this.scheduleRepository = scheduleRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AccessRuleResponse> getAll(UUID personId, UUID accessPointId, Pageable pageable) {
        if (personId != null) {
            return accessRuleRepository.findByPersonId(personId, pageable)
                    .map(AccessRuleResponse::fromEntity);
        }

        if (accessPointId != null) {
            return accessRuleRepository.findByAccessPointId(accessPointId, pageable)
                    .map(AccessRuleResponse::fromEntity);
        }

        return accessRuleRepository.findAll(pageable)
                .map(AccessRuleResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AccessRuleResponse getById(UUID id) {
        return AccessRuleResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public AccessRuleResponse create(AccessRuleCreateRequest request, HttpServletRequest httpRequest) {
        if (accessRuleRepository.existsByPersonIdAndAccessPointId(request.getPersonId(), request.getAccessPointId())) {
            throw new ConflictException("Access rule already exists for this person and access point");
        }

        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        AccessPoint accessPoint = accessPointRepository.findById(request.getAccessPointId())
                .orElseThrow(() -> new EntityNotFoundException("Access point not found"));

        Schedule schedule = findScheduleOrNull(request.getScheduleId());

        AccessRule rule = new AccessRule();
        rule.setPerson(person);
        rule.setAccessPoint(accessPoint);
        rule.setSchedule(schedule);
        rule.setValidFrom(request.getValidFrom());
        rule.setValidTo(request.getValidTo());
        rule.setActive(true);

        AccessRule saved = accessRuleRepository.save(rule);

        auditService.log("ACCESS_RULE_CREATED", "access_rules", saved.getId(), null, AccessRuleResponse.fromEntity(saved), httpRequest);

        return AccessRuleResponse.fromEntity(saved);
    }

    @Transactional
    public AccessRuleResponse update(UUID id, AccessRuleUpdateRequest request, HttpServletRequest httpRequest) {
        AccessRule rule = findEntity(id);
        AccessRuleResponse oldValue = AccessRuleResponse.fromEntity(rule);

        rule.setSchedule(findScheduleOrNull(request.getScheduleId()));
        rule.setValidFrom(request.getValidFrom());
        rule.setValidTo(request.getValidTo());

        if (request.getActive() != null) {
            rule.setActive(request.getActive());
        }

        AccessRule saved = accessRuleRepository.save(rule);

        auditService.log("ACCESS_RULE_UPDATED", "access_rules", saved.getId(), oldValue, AccessRuleResponse.fromEntity(saved), httpRequest);

        return AccessRuleResponse.fromEntity(saved);
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        AccessRule rule = findEntity(id);
        AccessRuleResponse oldValue = AccessRuleResponse.fromEntity(rule);

        rule.setActive(false);
        AccessRule saved = accessRuleRepository.save(rule);

        auditService.log("ACCESS_RULE_DEACTIVATED", "access_rules", id, oldValue, AccessRuleResponse.fromEntity(saved), httpRequest);
    }

    public AccessRule findEntity(UUID id) {
        return accessRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Access rule not found"));
    }

    private Schedule findScheduleOrNull(UUID scheduleId) {
        if (scheduleId == null) {
            return null;
        }

        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }
}