package ru.diamondshield_central.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_events")
public class AccessEvent {

    @Id
    @GeneratedValue
    private UUID id;

    // Локальный сервер, от которого пришло событие
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_server_id")
    private LocalServer localServer;

    // Идентификатор события на локальном сервере для дедупликации
    @Column(name = "local_event_id", length = 100)
    private String localEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private AccessObject object;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controller_id")
    private ControllerDevice controller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_id")
    private AccessIdentifier identifier;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(length = 20)
    private String direction;

    // allowed, denied, unknown
    @Column(name = "access_result", nullable = false, length = 50)
    private String accessResult;

    @Column(length = 255)
    private String reason;

    @Column(name = "identifier_type", length = 50)
    private String identifierType;

    @Column(name = "identifier_masked", length = 100)
    private String identifierMasked;

    // Хэш неизвестного идентификатора можно хранить для расследования
    @Column(name = "identifier_value_hash", columnDefinition = "text")
    private String identifierValueHash;

    @Column(name = "is_unknown_identifier", nullable = false)
    private Boolean unknownIdentifier;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AccessEvent() {
    }

    @PrePersist
    public void prePersist() {
        // created_at — время регистрации события на центральном сервере
        this.createdAt = LocalDateTime.now();

        if (this.unknownIdentifier == null) {
            this.unknownIdentifier = false;
        }
    }

    public UUID getId() {
        return id;
    }

    public LocalServer getLocalServer() {
        return localServer;
    }

    public void setLocalServer(LocalServer localServer) {
        this.localServer = localServer;
    }

    public String getLocalEventId() {
        return localEventId;
    }

    public void setLocalEventId(String localEventId) {
        this.localEventId = localEventId;
    }

    public AccessObject getObject() {
        return object;
    }

    public void setObject(AccessObject object) {
        this.object = object;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public ControllerDevice getController() {
        return controller;
    }

    public void setController(ControllerDevice controller) {
        this.controller = controller;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public AccessIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(AccessIdentifier identifier) {
        this.identifier = identifier;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAccessResult() {
        return accessResult;
    }

    public void setAccessResult(String accessResult) {
        this.accessResult = accessResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierMasked() {
        return identifierMasked;
    }

    public void setIdentifierMasked(String identifierMasked) {
        this.identifierMasked = identifierMasked;
    }

    public String getIdentifierValueHash() {
        return identifierValueHash;
    }

    public void setIdentifierValueHash(String identifierValueHash) {
        this.identifierValueHash = identifierValueHash;
    }

    public Boolean getUnknownIdentifier() {
        return unknownIdentifier;
    }

    public void setUnknownIdentifier(Boolean unknownIdentifier) {
        this.unknownIdentifier = unknownIdentifier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}