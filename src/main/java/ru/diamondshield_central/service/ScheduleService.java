package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.schedule.ScheduleCreateRequest;
import ru.diamondshield_central.dto.schedule.ScheduleIntervalRequest;
import ru.diamondshield_central.dto.schedule.ScheduleResponse;
import ru.diamondshield_central.dto.schedule.ScheduleUpdateRequest;
import ru.diamondshield_central.entity.Organization;
import ru.diamondshield_central.entity.Schedule;
import ru.diamondshield_central.entity.ScheduleInterval;
import ru.diamondshield_central.exception.BadRequestException;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.OrganizationRepository;
import ru.diamondshield_central.repository.ScheduleIntervalRepository;
import ru.diamondshield_central.repository.ScheduleRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleIntervalRepository scheduleIntervalRepository;
    private final OrganizationRepository organizationRepository;
    private final AuditService auditService;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           ScheduleIntervalRepository scheduleIntervalRepository,
                           OrganizationRepository organizationRepository,
                           AuditService auditService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleIntervalRepository = scheduleIntervalRepository;
        this.organizationRepository = organizationRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<ScheduleResponse> getAll(UUID organizationId, Pageable pageable) {
        Page<Schedule> page;

        if (organizationId != null) {
            page = scheduleRepository.findByOrganizationId(organizationId, pageable);
        } else {
            page = scheduleRepository.findAll(pageable);
        }

        return page.map(schedule -> ScheduleResponse.fromEntity(
                schedule,
                scheduleIntervalRepository.findByScheduleIdOrderByDayOfWeekAscStartTimeAsc(schedule.getId())
        ));
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getById(UUID id) {
        Schedule schedule = findEntity(id);
        List<ScheduleInterval> intervals =
                scheduleIntervalRepository.findByScheduleIdOrderByDayOfWeekAscStartTimeAsc(id);

        return ScheduleResponse.fromEntity(schedule, intervals);
    }

    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest request, HttpServletRequest httpRequest) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        if (scheduleRepository.existsByOrganizationIdAndNameIgnoreCase(request.getOrganizationId(), request.getName())) {
            throw new ConflictException("Schedule with this name already exists in organization");
        }

        Schedule schedule = new Schedule();
        schedule.setOrganization(organization);
        schedule.setName(request.getName());
        schedule.setDescription(request.getDescription());
        schedule.setActive(true);

        Schedule saved = scheduleRepository.save(schedule);

        saveIntervals(saved, request.getIntervals());

        ScheduleResponse response = getById(saved.getId());

        auditService.log("SCHEDULE_CREATED", "schedules", saved.getId(), null, response, httpRequest);

        return response;
    }

    @Transactional
    public ScheduleResponse update(UUID id, ScheduleUpdateRequest request, HttpServletRequest httpRequest) {
        Schedule schedule = findEntity(id);
        ScheduleResponse oldValue = getById(id);

        schedule.setName(request.getName());
        schedule.setDescription(request.getDescription());

        if (request.getActive() != null) {
            schedule.setActive(request.getActive());
        }

        Schedule saved = scheduleRepository.save(schedule);

        // Интервалы проще и надежнее полностью заменить
        scheduleIntervalRepository.deleteByScheduleId(saved.getId());
        saveIntervals(saved, request.getIntervals());

        ScheduleResponse response = getById(saved.getId());

        auditService.log("SCHEDULE_UPDATED", "schedules", saved.getId(), oldValue, response, httpRequest);

        return response;
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        Schedule schedule = findEntity(id);
        ScheduleResponse oldValue = getById(id);

        // Расписание не удаляется физически, чтобы не потерять историю
        schedule.setActive(false);

        Schedule saved = scheduleRepository.save(schedule);

        auditService.log("SCHEDULE_DEACTIVATED", "schedules", id, oldValue, ScheduleResponse.fromEntity(saved, List.of()), httpRequest);
    }

    public Schedule findEntity(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    private void saveIntervals(Schedule schedule, List<ScheduleIntervalRequest> intervals) {
        if (intervals == null) {
            return;
        }

        for (ScheduleIntervalRequest item : intervals) {
            if (!item.getStartTime().isBefore(item.getEndTime())) {
                throw new BadRequestException("Schedule interval startTime must be before endTime");
            }

            ScheduleInterval interval = new ScheduleInterval();
            interval.setSchedule(schedule);
            interval.setDayOfWeek(item.getDayOfWeek());
            interval.setStartTime(item.getStartTime());
            interval.setEndTime(item.getEndTime());

            scheduleIntervalRepository.save(interval);
        }
    }
}