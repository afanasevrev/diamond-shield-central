package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.Person;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    // Получение физических лиц конкретной организации
    Page<Person> findByOrganizationId(UUID organizationId, Pageable pageable);

    // Получение физических лиц организации по типу
    Page<Person> findByOrganizationIdAndPersonType(UUID organizationId, String personType, Pageable pageable);

    // Поиск по табельному номеру в рамках организации
    Optional<Person> findByOrganizationIdAndPersonnelNumber(UUID organizationId, String personnelNumber);

    // Проверка дубля табельного номера
    boolean existsByOrganizationIdAndPersonnelNumber(UUID organizationId, String personnelNumber);
}