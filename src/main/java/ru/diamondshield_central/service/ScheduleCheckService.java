package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.entity.Schedule;
import ru.diamondshield_central.entity.ScheduleInterval;
import ru.diamondshield_central.repository.ScheduleIntervalRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleCheckService {

    private final ScheduleIntervalRepository scheduleIntervalRepository;

    public ScheduleCheckService(ScheduleIntervalRepository scheduleIntervalRepository) {
        this.scheduleIntervalRepository = scheduleIntervalRepository;
    }

    @Transactional(readOnly = true)
    public boolean isAllowedBySchedule(Schedule schedule, LocalDateTime dateTime) {
        if (schedule == null) {
            // Если расписание не указано, трактуем это как круглосуточный доступ
            return true;
        }

        if (!Boolean.TRUE.equals(schedule.getActive())) {
            return false;
        }

        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalTime currentTime = dateTime.toLocalTime();

        List<ScheduleInterval> intervals =
                scheduleIntervalRepository.findByScheduleIdAndDayOfWeek(schedule.getId(), dayOfWeek);

        for (ScheduleInterval interval : intervals) {
            boolean afterOrEqualStart = !currentTime.isBefore(interval.getStartTime());
            boolean beforeOrEqualEnd = !currentTime.isAfter(interval.getEndTime());

            if (afterOrEqualStart && beforeOrEqualEnd) {
                return true;
            }
        }

        return false;
    }
}