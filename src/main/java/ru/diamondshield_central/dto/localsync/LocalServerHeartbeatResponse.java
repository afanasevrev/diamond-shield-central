package ru.diamondshield_central.dto.localsync;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalServerHeartbeatResponse {

    private UUID localServerId;
    private String status;
    private LocalDateTime serverTime;

    public LocalServerHeartbeatResponse() {
    }

    public LocalServerHeartbeatResponse(UUID localServerId, String status, LocalDateTime serverTime) {
        // serverTime нужен локальному серверу для сверки времени
        this.localServerId = localServerId;
        this.status = status;
        this.serverTime = serverTime;
    }

    public UUID getLocalServerId() {
        return localServerId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }
}