package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "local_servers")
public class LocalServer {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_id", nullable = false)
    private AccessObject object;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "mac_address", length = 50)
    private String macAddress;

    @Column(name = "server_token_hash", columnDefinition = "text")
    private String serverTokenHash;

    @Column(name = "software_version", length = 50)
    private String softwareVersion;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Признак, разрешена ли синхронизация этому локальному серверу
    @Column(name = "sync_enabled", nullable = false)
    private Boolean syncEnabled;

    // Время последней успешной синхронизации
    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    public LocalServer() {
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = "offline";
        }

        // По умолчанию синхронизация разрешена
        if (this.syncEnabled == null) {
            this.syncEnabled = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public AccessObject getObject() {
        return object;
    }

    public void setObject(AccessObject object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getServerTokenHash() {
        return serverTokenHash;
    }

    public void setServerTokenHash(String serverTokenHash) {
        this.serverTokenHash = serverTokenHash;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(LocalDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(Boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public LocalDateTime getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(LocalDateTime lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}