package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.localsync.*;
import ru.diamondshield_central.entity.*;
import ru.diamondshield_central.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LocalConfigService {

    private final ControllerDeviceRepository controllerDeviceRepository;
    private final ReaderRepository readerRepository;
    private final AccessPointRepository accessPointRepository;
    private final PersonRepository personRepository;
    private final AccessIdentifierRepository accessIdentifierRepository;
    private final AccessRuleRepository accessRuleRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleIntervalRepository scheduleIntervalRepository;
    private final LocalServerRepository localServerRepository;

    public LocalConfigService(ControllerDeviceRepository controllerDeviceRepository,
                              ReaderRepository readerRepository,
                              AccessPointRepository accessPointRepository,
                              PersonRepository personRepository,
                              AccessIdentifierRepository accessIdentifierRepository,
                              AccessRuleRepository accessRuleRepository,
                              ScheduleRepository scheduleRepository,
                              ScheduleIntervalRepository scheduleIntervalRepository,
                              LocalServerRepository localServerRepository) {
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.readerRepository = readerRepository;
        this.accessPointRepository = accessPointRepository;
        this.personRepository = personRepository;
        this.accessIdentifierRepository = accessIdentifierRepository;
        this.accessRuleRepository = accessRuleRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleIntervalRepository = scheduleIntervalRepository;
        this.localServerRepository = localServerRepository;
    }

    @Transactional(readOnly = true)
    public LocalConfigResponse buildConfig(LocalServer localServer) {
        UUID objectId = localServer.getObject().getId();
        UUID organizationId = localServer.getObject().getOrganization().getId();

        List<ControllerDevice> controllers =
                controllerDeviceRepository.findByObjectId(objectId, org.springframework.data.domain.Pageable.unpaged()).getContent();

        List<Reader> readers = controllers.stream()
                .flatMap(controller -> readerRepository
                        .findByControllerId(controller.getId(), org.springframework.data.domain.Pageable.unpaged())
                        .getContent()
                        .stream())
                .toList();

        List<AccessPoint> accessPoints =
                accessPointRepository.findByObjectId(objectId, org.springframework.data.domain.Pageable.unpaged()).getContent();

        List<Person> persons =
                personRepository.findByOrganizationId(organizationId, org.springframework.data.domain.Pageable.unpaged()).getContent();

        List<AccessIdentifier> identifiers = persons.stream()
                .flatMap(person -> accessIdentifierRepository
                        .findByPersonId(person.getId(), org.springframework.data.domain.Pageable.unpaged())
                        .getContent()
                        .stream())
                .toList();

        List<AccessRule> rules = persons.stream()
                .flatMap(person -> accessRuleRepository
                        .findByPersonId(person.getId(), org.springframework.data.domain.Pageable.unpaged())
                        .getContent()
                        .stream())
                .filter(rule -> rule.getAccessPoint() != null && rule.getAccessPoint().getObject().getId().equals(objectId))
                .toList();

        List<Schedule> schedules =
                scheduleRepository.findByOrganizationId(organizationId, org.springframework.data.domain.Pageable.unpaged()).getContent();

        List<ScheduleInterval> intervals = schedules.stream()
                .flatMap(schedule -> scheduleIntervalRepository
                        .findByScheduleIdOrderByDayOfWeekAscStartTimeAsc(schedule.getId())
                        .stream())
                .toList();

        LocalConfigResponse response = new LocalConfigResponse();

        // generatedAt нужен локальному серверу для понимания актуальности конфигурации
        response.setLocalServerId(localServer.getId());
        response.setObjectId(objectId);
        response.setGeneratedAt(LocalDateTime.now());

        response.setControllers(controllers.stream().map(LocalConfigControllerDto::fromEntity).toList());
        response.setReaders(readers.stream().map(LocalConfigReaderDto::fromEntity).toList());
        response.setAccessPoints(accessPoints.stream().map(LocalConfigAccessPointDto::fromEntity).toList());
        response.setPersons(persons.stream().map(LocalConfigPersonDto::fromEntity).toList());
        response.setIdentifiers(identifiers.stream().map(LocalConfigIdentifierDto::fromEntity).toList());
        response.setAccessRules(rules.stream().map(LocalConfigAccessRuleDto::fromEntity).toList());
        response.setSchedules(schedules.stream().map(LocalConfigScheduleDto::fromEntity).toList());
        response.setScheduleIntervals(intervals.stream().map(LocalConfigScheduleIntervalDto::fromEntity).toList());

        return response;
    }

    @Transactional
    public void markConfigPulled(LocalServer localServer) {
        // Фиксируем успешную выдачу конфигурации
        localServer.setLastSyncAt(LocalDateTime.now());
        localServerRepository.save(localServer);
    }
}