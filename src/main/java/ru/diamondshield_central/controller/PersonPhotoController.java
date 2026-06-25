package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.diamondshield_central.dto.photo.PersonPhotoResponse;
import ru.diamondshield_central.entity.PersonPhoto;
import ru.diamondshield_central.service.PersonPhotoService;

import java.util.List;
import java.util.UUID;

@RestController
public class PersonPhotoController {

    private final PersonPhotoService personPhotoService;

    public PersonPhotoController(PersonPhotoService personPhotoService) {
        this.personPhotoService = personPhotoService;
    }

    @GetMapping("/api/persons/{personId}/photos")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_PHOTO_VIEW')")
    public List<PersonPhotoResponse> getByPerson(@PathVariable UUID personId) {
        return personPhotoService.getByPerson(personId);
    }

    @PostMapping(value = "/api/persons/{personId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_PHOTO_MANAGE')")
    public PersonPhotoResponse upload(@PathVariable UUID personId,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam(required = false) Boolean main,
                                      HttpServletRequest httpRequest) {
        return personPhotoService.upload(personId, file, main, httpRequest);
    }

    @GetMapping("/api/person-photos/{photoId}/data")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_PHOTO_VIEW')")
    public ResponseEntity<byte[]> getPhotoData(@PathVariable UUID photoId) {
        PersonPhoto photo = personPhotoService.getPhotoEntity(photoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photo.getContentType() == null ? "application/octet-stream" : photo.getContentType()));
        headers.setContentLength(photo.getFileSize());

        // Возвращаем бинарные данные фотографии
        return new ResponseEntity<>(photo.getPhotoData(), headers, HttpStatus.OK);
    }

    @PostMapping("/api/person-photos/{photoId}/set-main")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_PHOTO_MANAGE')")
    public PersonPhotoResponse setMain(@PathVariable UUID photoId,
                                       HttpServletRequest httpRequest) {
        return personPhotoService.setMain(photoId, httpRequest);
    }

    @DeleteMapping("/api/person-photos/{photoId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_PHOTO_MANAGE')")
    public void delete(@PathVariable UUID photoId,
                       HttpServletRequest httpRequest) {
        personPhotoService.delete(photoId, httpRequest);
    }
}