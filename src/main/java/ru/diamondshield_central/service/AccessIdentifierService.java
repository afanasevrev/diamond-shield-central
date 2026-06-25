package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.identifier.AccessIdentifierCreateRequest;
import ru.diamondshield_central.dto.identifier.AccessIdentifierResponse;
import ru.diamondshield_central.dto.identifier.IdentifierUniqueCheckResponse;
import ru.diamondshield_central.entity.AccessIdentifier;
import ru.diamondshield_central.entity.Person;
import ru.diamondshield_central.entity.Reader;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.exception.ConflictException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessIdentifierRepository;
import ru.diamondshield_central.repository.PersonRepository;
import ru.diamondshield_central.repository.ReaderRepository;
import ru.diamondshield_central.security.CustomUserDetails;

import java.util.UUID;

@Service
public class AccessIdentifierService {

    private final AccessIdentifierRepository accessIdentifierRepository;
    private final PersonRepository personRepository;
    private final ReaderRepository readerRepository;
    private final IdentifierSecurityService identifierSecurityService;
    private final AuditService auditService;

    public AccessIdentifierService(AccessIdentifierRepository accessIdentifierRepository,
                                   PersonRepository personRepository,
                                   ReaderRepository readerRepository,
                                   IdentifierSecurityService identifierSecurityService,
                                   AuditService auditService) {
        this.accessIdentifierRepository = accessIdentifierRepository;
        this.personRepository = personRepository;
        this.readerRepository = readerRepository;
        this.identifierSecurityService = identifierSecurityService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<AccessIdentifierResponse> getAll(UUID personId, Pageable pageable) {
        if (personId != null) {
            return accessIdentifierRepository.findByPersonId(personId, pageable)
                    .map(AccessIdentifierResponse::fromEntity);
        }

        return accessIdentifierRepository.findAll(pageable)
                .map(AccessIdentifierResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public AccessIdentifierResponse getById(UUID id) {
        return AccessIdentifierResponse.fromEntity(findEntity(id));
    }

    @Transactional(readOnly = true)
    public IdentifierUniqueCheckResponse checkUnique(String identifierType, String identifierValue) {
        String normalized = identifierSecurityService.normalize(identifierValue);
        String hash = identifierSecurityService.sha256(normalized);

        boolean exists = accessIdentifierRepository
                .existsByIdentifierTypeAndIdentifierValueHash(identifierType, hash);

        if (exists) {
            return new IdentifierUniqueCheckResponse(false, "Identifier already exists");
        }

        return new IdentifierUniqueCheckResponse(true, "Identifier is unique");
    }

    @Transactional
    public AccessIdentifierResponse create(AccessIdentifierCreateRequest request,
                                           HttpServletRequest httpRequest) {
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        String normalized = identifierSecurityService.normalize(request.getIdentifierValue());
        String hash = identifierSecurityService.sha256(normalized);

        if (accessIdentifierRepository.existsByIdentifierTypeAndIdentifierValueHash(request.getIdentifierType(), hash)) {
            // Требование ТЗ: повторная привязка запрещена
            auditService.log("IDENTIFIER_DUPLICATE_ATTEMPT", "access_identifiers", null, null, request.getIdentifierType(), httpRequest);
            throw new ConflictException("Identifier already exists");
        }

        Reader reader = null;
        if (request.getReaderId() != null) {
            reader = readerRepository.findById(request.getReaderId())
                    .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        }

        AccessIdentifier identifier = new AccessIdentifier();
        identifier.setPerson(person);
        identifier.setIdentifierType(request.getIdentifierType());
        identifier.setIdentifierValueHash(hash);
        identifier.setIdentifierMasked(identifierSecurityService.mask(normalized));
        identifier.setValidFrom(request.getValidFrom());
        identifier.setValidTo(request.getValidTo());
        identifier.setStatus("active");
        identifier.setReader(reader);
        identifier.setIssuedBy(getCurrentUserOrNull());
        identifier.setComment(request.getComment());

        AccessIdentifier saved = accessIdentifierRepository.save(identifier);

        auditService.log("IDENTIFIER_CREATED", "access_identifiers", saved.getId(), null, AccessIdentifierResponse.fromEntity(saved), httpRequest);

        return AccessIdentifierResponse.fromEntity(saved);
    }

    @Transactional
    public AccessIdentifierResponse block(UUID id, HttpServletRequest httpRequest) {
        AccessIdentifier identifier = findEntity(id);
        AccessIdentifierResponse oldValue = AccessIdentifierResponse.fromEntity(identifier);

        identifier.setStatus("blocked");
        AccessIdentifier saved = accessIdentifierRepository.save(identifier);

        auditService.log("IDENTIFIER_BLOCKED", "access_identifiers", saved.getId(), oldValue, AccessIdentifierResponse.fromEntity(saved), httpRequest);

        return AccessIdentifierResponse.fromEntity(saved);
    }

    @Transactional
    public AccessIdentifierResponse deactivate(UUID id, HttpServletRequest httpRequest) {
        AccessIdentifier identifier = findEntity(id);
        AccessIdentifierResponse oldValue = AccessIdentifierResponse.fromEntity(identifier);

        // Логическое удаление через статус
        identifier.setStatus("deleted");
        AccessIdentifier saved = accessIdentifierRepository.save(identifier);

        auditService.log("IDENTIFIER_DEACTIVATED", "access_identifiers", saved.getId(), oldValue, AccessIdentifierResponse.fromEntity(saved), httpRequest);

        return AccessIdentifierResponse.fromEntity(saved);
    }

    public AccessIdentifier findEntity(UUID id) {
        return accessIdentifierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Identifier not found"));
    }

    private SystemUser getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }
}