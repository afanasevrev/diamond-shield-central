package ru.diamondshield_central.dto.person;

import ru.diamondshield_central.entity.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PersonResponse {

    private UUID id;
    private UUID organizationId;
    private String organizationName;
    private String personType;
    private String personnelNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String documentType;
    private String documentSeries;
    private String documentNumber;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PersonResponse() {
    }

    public static PersonResponse fromEntity(Person person) {
        PersonResponse response = new PersonResponse();

        // Entity преобразуем в безопасный DTO без ленивых Hibernate-прокси наружу
        response.id = person.getId();

        if (person.getOrganization() != null) {
            response.organizationId = person.getOrganization().getId();
            response.organizationName = person.getOrganization().getName();
        }

        response.personType = person.getPersonType();
        response.personnelNumber = person.getPersonnelNumber();
        response.lastName = person.getLastName();
        response.firstName = person.getFirstName();
        response.middleName = person.getMiddleName();
        response.birthDate = person.getBirthDate();
        response.phone = person.getPhone();
        response.email = person.getEmail();
        response.documentType = person.getDocumentType();
        response.documentSeries = person.getDocumentSeries();
        response.documentNumber = person.getDocumentNumber();
        response.active = person.getActive();
        response.createdAt = person.getCreatedAt();
        response.updatedAt = person.getUpdatedAt();

        return response;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getPersonType() {
        return personType;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}