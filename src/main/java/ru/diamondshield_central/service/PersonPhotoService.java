package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.diamondshield_central.dto.photo.PersonPhotoResponse;
import ru.diamondshield_central.entity.Person;
import ru.diamondshield_central.entity.PersonPhoto;
import ru.diamondshield_central.exception.BadRequestException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.PersonPhotoRepository;
import ru.diamondshield_central.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PersonPhotoService {

    private static final int MAX_PHOTO_SIZE_BYTES = 102400;

    private final PersonPhotoRepository personPhotoRepository;
    private final PersonRepository personRepository;
    private final AuditService auditService;

    public PersonPhotoService(PersonPhotoRepository personPhotoRepository,
                              PersonRepository personRepository,
                              AuditService auditService) {
        this.personPhotoRepository = personPhotoRepository;
        this.personRepository = personRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<PersonPhotoResponse> getByPerson(UUID personId) {
        return personPhotoRepository.findByPersonId(personId)
                .stream()
                .map(PersonPhotoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonPhoto getPhotoEntity(UUID photoId) {
        return personPhotoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
    }

    @Transactional
    public PersonPhotoResponse upload(UUID personId,
                                      MultipartFile file,
                                      Boolean main,
                                      HttpServletRequest httpRequest) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Photo file is empty");
        }

        if (file.getSize() > MAX_PHOTO_SIZE_BYTES) {
            throw new BadRequestException("Photo size must not exceed 100 KB");
        }

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        try {
            if (Boolean.TRUE.equals(main)) {
                // Если новая фотография основная, снимаем этот признак с остальных
                clearMainPhotos(personId);
            }

            PersonPhoto photo = new PersonPhoto();
            photo.setPerson(person);
            photo.setFileName(file.getOriginalFilename());
            photo.setContentType(file.getContentType());
            photo.setFileSize((int) file.getSize());
            photo.setPhotoData(file.getBytes());
            photo.setMain(Boolean.TRUE.equals(main));

            PersonPhoto saved = personPhotoRepository.save(photo);

            auditService.log("PERSON_PHOTO_UPLOADED", "person_photos", saved.getId(), null, PersonPhotoResponse.fromEntity(saved), httpRequest);

            return PersonPhotoResponse.fromEntity(saved);
        } catch (Exception ex) {
            throw new BadRequestException("Cannot upload photo");
        }
    }

    @Transactional
    public PersonPhotoResponse setMain(UUID photoId, HttpServletRequest httpRequest) {
        PersonPhoto photo = getPhotoEntity(photoId);

        // Основная фотография должна быть только одна
        clearMainPhotos(photo.getPerson().getId());

        photo.setMain(true);
        PersonPhoto saved = personPhotoRepository.save(photo);

        auditService.log("PERSON_PHOTO_SET_MAIN", "person_photos", saved.getId(), null, PersonPhotoResponse.fromEntity(saved), httpRequest);

        return PersonPhotoResponse.fromEntity(saved);
    }

    @Transactional
    public void delete(UUID photoId, HttpServletRequest httpRequest) {
        PersonPhoto photo = getPhotoEntity(photoId);
        PersonPhotoResponse oldValue = PersonPhotoResponse.fromEntity(photo);

        personPhotoRepository.delete(photo);

        auditService.log("PERSON_PHOTO_DELETED", "person_photos", photoId, oldValue, null, httpRequest);
    }

    private void clearMainPhotos(UUID personId) {
        List<PersonPhoto> photos = personPhotoRepository.findByPersonIdAndMainTrue(personId);

        for (PersonPhoto item : photos) {
            item.setMain(false);
            personPhotoRepository.save(item);
        }
    }
}