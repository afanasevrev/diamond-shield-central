package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_points")
public class AccessPoint {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_id", nullable = false)
    private AccessObject object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_from_id")
    private ObjectZone zoneFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_to_id")
    private ObjectZone zoneTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controller_id")
    private ControllerDevice controller;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "access_point_type", length = 50)
    private String accessPointType;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AccessPoint() {
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = "offline";
        }

        if (this.active == null) {
            this.active = true;
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

    public ObjectZone getZoneFrom() {
        return zoneFrom;
    }

    public void setZoneFrom(ObjectZone zoneFrom) {
        this.zoneFrom = zoneFrom;
    }

    public ObjectZone getZoneTo() {
        return zoneTo;
    }

    public void setZoneTo(ObjectZone zoneTo) {
        this.zoneTo = zoneTo;
    }

    public ControllerDevice getController() {
        return controller;
    }

    public void setController(ControllerDevice controller) {
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessPointType() {
        return accessPointType;
    }

    public void setAccessPointType(String accessPointType) {
        this.accessPointType = accessPointType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}