package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.ScheduleInterval;

import java.util.List;
import java.util.UUID;

public interface ScheduleIntervalRepository extends JpaRepository<ScheduleInterval, UUID> {

    // Получить все интервалы расписания
    List<ScheduleInterval> findByScheduleIdOrderByDayOfWeekAscStartTimeAsc(UUID scheduleId);

    // Удалить интервалы при полном обновлении расписания
    void deleteByScheduleId(UUID scheduleId);
}