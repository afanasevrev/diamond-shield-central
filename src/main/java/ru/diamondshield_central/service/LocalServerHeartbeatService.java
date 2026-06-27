package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.localsync.LocalServerHeartbeatRequest;
import ru.diamondshield_central.dto.localsync.LocalServerHeartbeatResponse;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.repository.LocalServerRepository;

import java.time.LocalDateTime;

@Service
public class LocalServerHeartbeatService {

    private final LocalServerRepository localServerRepository;

    public LocalServerHeartbeatService(LocalServerRepository localServerRepository) {
        this.localServerRepository = localServerRepository;
    }

    @Transactional
    public LocalServerHeartbeatResponse heartbeat(LocalServer localServer,
                                                  LocalServerHeartbeatRequest request) {
        if (request.getIpAddress() != null) {
            localServer.setIpAddress(request.getIpAddress());
        }

        if (request.getSoftwareVersion() != null) {
            localServer.setSoftwareVersion(request.getSoftwareVersion());
        }

        // Если статус не передан, считаем сервер online
        localServer.setStatus(request.getStatus() == null ? "online" : request.getStatus());
        localServer.setLastSeenAt(LocalDateTime.now());

        LocalServer saved = localServerRepository.save(localServer);

        return new LocalServerHeartbeatResponse(
                saved.getId(),
                saved.getStatus(),
                LocalDateTime.now()
        );
    }
}