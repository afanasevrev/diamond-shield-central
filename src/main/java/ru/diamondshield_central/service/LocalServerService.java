package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.localserver.LocalServerCreateRequest;
import ru.diamondshield_central.dto.localserver.LocalServerResponse;
import ru.diamondshield_central.dto.localserver.LocalServerUpdateRequest;
import ru.diamondshield_central.entity.AccessObject;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.AccessObjectRepository;
import ru.diamondshield_central.repository.LocalServerRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LocalServerService {

    private final LocalServerRepository localServerRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AuditService auditService;

    public LocalServerService(LocalServerRepository localServerRepository,
                              AccessObjectRepository accessObjectRepository,
                              AuditService auditService) {
        this.localServerRepository = localServerRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<LocalServerResponse> getAll(UUID objectId, Pageable pageable) {
        if (objectId != null) {
            return localServerRepository.findByObjectId(objectId, pageable)
                    .map(LocalServerResponse::fromEntity);
        }

        return localServerRepository.findAll(pageable)
                .map(LocalServerResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public LocalServerResponse getById(UUID id) {
        return LocalServerResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public LocalServerResponse create(LocalServerCreateRequest request, HttpServletRequest httpRequest) {
        AccessObject object = accessObjectRepository.findById(request.getObjectId())
                .orElseThrow(() -> new EntityNotFoundException("Object not found"));

        LocalServer localServer = new LocalServer();
        localServer.setObject(object);
        localServer.setName(request.getName());
        localServer.setIpAddress(request.getIpAddress());
        localServer.setMacAddress(request.getMacAddress());
        localServer.setServerTokenHash(request.getServerTokenHash());
        localServer.setSoftwareVersion(request.getSoftwareVersion());
        localServer.setStatus("offline");

        LocalServer saved = localServerRepository.save(localServer);

        auditService.log("LOCAL_SERVER_CREATED", "local_servers", saved.getId(), null, saved, httpRequest);

        return LocalServerResponse.fromEntity(saved);
    }

    @Transactional
    public LocalServerResponse update(UUID id,
                                      LocalServerUpdateRequest request,
                                      HttpServletRequest httpRequest) {
        LocalServer localServer = findEntity(id);
        LocalServer oldValue = copy(localServer);

        localServer.setName(request.getName());
        localServer.setIpAddress(request.getIpAddress());
        localServer.setMacAddress(request.getMacAddress());
        localServer.setServerTokenHash(request.getServerTokenHash());
        localServer.setSoftwareVersion(request.getSoftwareVersion());

        if (request.getStatus() != null) {
            localServer.setStatus(request.getStatus());
        }

        LocalServer saved = localServerRepository.save(localServer);

        auditService.log("LOCAL_SERVER_UPDATED", "local_servers", saved.getId(), oldValue, saved, httpRequest);

        return LocalServerResponse.fromEntity(saved);
    }

    @Transactional
    public LocalServerResponse markSeen(UUID id, HttpServletRequest httpRequest) {
        LocalServer localServer = findEntity(id);
        LocalServer oldValue = copy(localServer);

        localServer.setStatus("online");
        localServer.setLastSeenAt(LocalDateTime.now());

        LocalServer saved = localServerRepository.save(localServer);

        auditService.log("LOCAL_SERVER_SEEN", "local_servers", saved.getId(), oldValue, saved, httpRequest);

        return LocalServerResponse.fromEntity(saved);
    }

    @Transactional
    public void setOffline(UUID id, HttpServletRequest httpRequest) {
        LocalServer localServer = findEntity(id);
        LocalServer oldValue = copy(localServer);

        localServer.setStatus("offline");
        localServerRepository.save(localServer);

        auditService.log("LOCAL_SERVER_OFFLINE", "local_servers", id, oldValue, localServer, httpRequest);
    }

    public LocalServer findEntity(UUID id) {
        return localServerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Local server not found"));
    }

    private LocalServer copy(LocalServer source) {
        LocalServer copy = new LocalServer();
        copy.setObject(source.getObject());
        copy.setName(source.getName());
        copy.setIpAddress(source.getIpAddress());
        copy.setMacAddress(source.getMacAddress());
        copy.setServerTokenHash(source.getServerTokenHash());
        copy.setSoftwareVersion(source.getSoftwareVersion());
        copy.setStatus(source.getStatus());
        copy.setLastSeenAt(source.getLastSeenAt());
        return copy;
    }
}