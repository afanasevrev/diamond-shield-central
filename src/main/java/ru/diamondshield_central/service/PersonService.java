package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.person.PersonCreateRequest;
import ru.diamondshield_central.dto.person.PersonResponse;
import ru.diamondshield_central.dto.person.PersonUpdateRequest;
import ru.diamondshield_central.entity.Organization;
import ru.diamondshield_central.entity.Person;
import ru.diamondshield_central.exception.BadRequestException;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.OrganizationRepository;
import ru.diamondshield_central.repository.PersonRepository;

import java.util.Set;
import java.util.UUID;

@Service
public class PersonService {

    private static final Set<String> ALLOWED_PERSON_TYPES = Set.of("employee", "resident", "guest");

    private final PersonRepository personRepository;
    private final OrganizationRepository organizationRepository;
    private final AuditService auditService;

    public PersonService(PersonRepository personRepository,
                         OrganizationRepository organizationRepository,
                         AuditService auditService) {
        this.personRepository = personRepository;
        this.organizationRepository = organizationRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<PersonResponse> getAll(UUID organizationId, String personType, Pageable pageable) {
        if (organizationId != null && personType != null) {
            return personRepository.findByOrganizationIdAndPersonType(organizationId, personType, pageable)
                    .map(PersonResponse::fromEntity);
        }

        if (organizationId != null) {
            return personRepository.findByOrganizationId(organizationId, pageable)
                    .map(PersonResponse::fromEntity);
        }

        return personRepository.findAll(pageable)
                .map(PersonResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public PersonResponse getById(UUID id) {
        return PersonResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public PersonResponse create(PersonCreateRequest request, HttpServletRequest httpRequest) {
        validatePersonType(request.getPersonType());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        validatePersonnelNumberUnique(
                request.getOrganizationId(),
                request.getPersonnelNumber(),
                null
        );

        Person person = new Person();
        person.setOrganization(organization);
        person.setPersonType(request.getPersonType());
        person.setPersonnelNumber(emptyToNull(request.getPersonnelNumber()));
        person.setLastName(request.getLastName());
        person.setFirstName(request.getFirstName());
        person.setMiddleName(request.getMiddleName());
        person.setBirthDate(request.getBirthDate());
        person.setPhone(request.getPhone());
        person.setEmail(request.getEmail());
        person.setDocumentType(request.getDocumentType());
        person.setDocumentSeries(request.getDocumentSeries());
        person.setDocumentNumber(request.getDocumentNumber());
        person.setActive(true);

        Person saved = personRepository.save(person);

        auditService.log("PERSON_CREATED", "persons", saved.getId(), null, PersonResponse.fromEntity(saved), httpRequest);

        return PersonResponse.fromEntity(saved);
    }

    @Transactional
    public PersonResponse update(UUID id, PersonUpdateRequest request, HttpServletRequest httpRequest) {
        validatePersonType(request.getPersonType());

        Person person = findEntity(id);
        PersonResponse oldValue = PersonResponse.fromEntity(person);

        validatePersonnelNumberUnique(
                person.getOrganization().getId(),
                request.getPersonnelNumber(),
                person.getId()
        );

        person.setPersonType(request.getPersonType());
        person.setPersonnelNumber(emptyToNull(request.getPersonnelNumber()));
        person.setLastName(request.getLastName());
        person.setFirstName(request.getFirstName());
        person.setMiddleName(request.getMiddleName());
        person.setBirthDate(request.getBirthDate());
        person.setPhone(request.getPhone());
        person.setEmail(request.getEmail());
        person.setDocumentType(request.getDocumentType());
        person.setDocumentSeries(request.getDocumentSeries());
        person.setDocumentNumber(request.getDocumentNumber());

        if (request.getActive() != null) {
            person.setActive(request.getActive());
        }

        Person saved = personRepository.save(person);

        auditService.log("PERSON_UPDATED", "persons", saved.getId(), oldValue, PersonResponse.fromEntity(saved), httpRequest);

        return PersonResponse.fromEntity(saved);
    }

    @Transactional
    public void deactivate(UUID id, HttpServletRequest httpRequest) {
        Person person = findEntity(id);
        PersonResponse oldValue = PersonResponse.fromEntity(person);

        // Критичные данные не удаляем физически, а деактивируем
        person.setActive(false);

        Person saved = personRepository.save(person);

        auditService.log("PERSON_DEACTIVATED", "persons", id, oldValue, PersonResponse.fromEntity(saved), httpRequest);
    }

    public Person findEntity(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }

    public void validatePersonType(String personType) {
        if (personType == null || !ALLOWED_PERSON_TYPES.contains(personType)) {
            throw new BadRequestException("Invalid person_type. Allowed values: employee, resident, guest");
        }
    }

    private void validatePersonnelNumberUnique(UUID organizationId, String personnelNumber, UUID currentPersonId) {
        String normalized = emptyToNull(personnelNumber);

        if (normalized == null) {
            return;
        }

        personRepository.findByOrganizationIdAndPersonnelNumber(organizationId, normalized)
                .ifPresent(existing -> {
                    // При обновлении разрешаем оставить свой же табельный номер
                    if (currentPersonId == null || !existing.getId().equals(currentPersonId)) {
                        throw new ConflictException("Personnel number already exists in organization");
                    }
                });
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}