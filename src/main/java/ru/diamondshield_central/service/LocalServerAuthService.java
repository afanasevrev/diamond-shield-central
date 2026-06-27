package ru.diamondshield_central.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.exception.BadRequestException;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.LocalServerRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LocalServerAuthService {

    private final LocalServerRepository localServerRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalServerAuthService(LocalServerRepository localServerRepository,
                                  PasswordEncoder passwordEncoder) {
        this.localServerRepository = localServerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LocalServer authenticate(UUID localServerId, String token) {
        if (localServerId == null) {
            throw new BadRequestException("X-Local-Server-Id header is required");
        }

        if (token == null || token.isBlank()) {
            throw new BadRequestException("X-Local-Server-Token header is required");
        }

        LocalServer localServer = localServerRepository.findById(localServerId)
                .orElseThrow(() -> new EntityNotFoundException("Local server not found"));

        if (!Boolean.TRUE.equals(localServer.getSyncEnabled())) {
            throw new BadRequestException("Local server synchronization is disabled");
        }

        if (localServer.getServerTokenHash() == null ||
                !passwordEncoder.matches(token, localServer.getServerTokenHash())) {
            throw new BadRequestException("Invalid local server token");
        }

        // При успешной проверке фиксируем активность сервера
        localServer.setStatus("online");
        localServer.setLastSeenAt(LocalDateTime.now());
        localServerRepository.save(localServer);

        return localServer;
    }
}