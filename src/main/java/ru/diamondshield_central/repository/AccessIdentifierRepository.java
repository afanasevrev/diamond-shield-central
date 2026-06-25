package ru.diamondshield_central.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.AccessIdentifier;

import java.util.Optional;
import java.util.UUID;

public interface AccessIdentifierRepository extends JpaRepository<AccessIdentifier, UUID> {

    // Получить идентификаторы конкретного физического лица
    Page<AccessIdentifier> findByPersonId(UUID personId, Pageable pageable);

    // Проверить, существует ли идентификатор с таким типом и хэшем
    boolean existsByIdentifierTypeAndIdentifierValueHash(String identifierType, String identifierValueHash);

    // Найти идентификатор по типу и хэшу
    Optional<AccessIdentifier> findByIdentifierTypeAndIdentifierValueHash(String identifierType, String identifierValueHash);
}