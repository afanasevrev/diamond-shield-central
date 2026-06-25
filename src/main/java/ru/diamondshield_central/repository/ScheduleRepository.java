package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.Schedule;

import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    // Получить расписания организации
    Page<Schedule> findByOrganizationId(UUID organizationId, Pageable pageable);

    // Проверить дубль имени расписания внутри организации
    boolean existsByOrganizationIdAndNameIgnoreCase(UUID organizationId, String name);
}