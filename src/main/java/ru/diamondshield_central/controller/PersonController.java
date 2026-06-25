package ru.diamondshield_central.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.diamondshield_central.dto.imports.PersonImportResponse;
import ru.diamondshield_central.dto.person.PersonCreateRequest;
import ru.diamondshield_central.dto.person.PersonResponse;
import ru.diamondshield_central.dto.person.PersonUpdateRequest;
import ru.diamondshield_central.service.PersonImportService;
import ru.diamondshield_central.service.PersonService;

import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonImportService personImportService;

    public PersonController(PersonService personService,
                            PersonImportService personImportService) {
        this.personService = personService;
        this.personImportService = personImportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_VIEW')")
    public Page<PersonResponse> getAll(@RequestParam(required = false) UUID organizationId,
                                       @RequestParam(required = false) String personType,
                                       Pageable pageable) {
        // Фильтры позволяют получить людей по организации и типу
        return personService.getAll(organizationId, personType, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_VIEW')")
    public PersonResponse getById(@PathVariable UUID id) {
        return personService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_MANAGE')")
    public PersonResponse create(@Valid @RequestBody PersonCreateRequest request,
                                 HttpServletRequest httpRequest) {
        return personService.create(request, httpRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_MANAGE')")
    public PersonResponse update(@PathVariable UUID id,
                                 @Valid @RequestBody PersonUpdateRequest request,
                                 HttpServletRequest httpRequest) {
        return personService.update(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_MANAGE')")
    public void deactivate(@PathVariable UUID id,
                           HttpServletRequest httpRequest) {
        // Удаление выполняется логически через is_active=false
        personService.deactivate(id, httpRequest);
    }

    @PostMapping("/import/xlsx")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('PERSON_IMPORT')")
    public PersonImportResponse importXlsx(@RequestParam UUID organizationId,
                                           @RequestParam("file") MultipartFile file,
                                           HttpServletRequest httpRequest) {
        return personImportService.importPersons(organizationId, file, httpRequest);
    }
}