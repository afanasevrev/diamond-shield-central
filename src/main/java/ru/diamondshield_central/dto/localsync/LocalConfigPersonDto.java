package ru.diamondshield_central.dto.localsync;

import ru.diamondshield_central.entity.Person;

import java.util.UUID;

public class LocalConfigPersonDto {

    private UUID id;
    private String personType;
    private String personnelNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private Boolean active;

    public static LocalConfigPersonDto fromEntity(Person person) {
        LocalConfigPersonDto dto = new LocalConfigPersonDto();

        // На локальный сервер не отправляем документы, телефон и email
        dto.id = person.getId();
        dto.personType = person.getPersonType();
        dto.personnelNumber = person.getPersonnelNumber();
        dto.lastName = person.getLastName();
        dto.firstName = person.getFirstName();
        dto.middleName = person.getMiddleName();
        dto.active = person.getActive();

        return dto;
    }

    public UUID getId() {
        return id;
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

    public Boolean getActive() {
        return active;
    }
}