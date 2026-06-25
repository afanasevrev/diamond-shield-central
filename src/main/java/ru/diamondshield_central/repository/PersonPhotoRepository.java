package ru.diamondshield_central.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diamondshield_central.entity.PersonPhoto;

import java.util.List;
import java.util.UUID;

public interface PersonPhotoRepository extends JpaRepository<PersonPhoto, UUID> {

    // Получить все фотографии физического лица
    List<PersonPhoto> findByPersonId(UUID personId);

    // Получить основную фотографию физического лица
    List<PersonPhoto> findByPersonIdAndMainTrue(UUID personId);
}