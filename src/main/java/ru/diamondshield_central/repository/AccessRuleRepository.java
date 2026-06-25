package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AccessRule;

import java.util.Optional;
import java.util.UUID;

public interface AccessRuleRepository extends JpaRepository<AccessRule, UUID> {

    // Правила конкретного физического лица
    Page<AccessRule> findByPersonId(UUID personId, Pageable pageable);

    // Правила конкретной точки прохода
    Page<AccessRule> findByAccessPointId(UUID accessPointId, Pageable pageable);

    // Проверка дубля правила
    boolean existsByPersonIdAndAccessPointId(UUID personId, UUID accessPointId);

    // Поиск правила для проверки доступа
    Optional<AccessRule> findByPersonIdAndAccessPointId(UUID personId, UUID accessPointId);
}