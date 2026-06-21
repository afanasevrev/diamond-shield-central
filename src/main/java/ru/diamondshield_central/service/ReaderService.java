package ru.diamondshield_central.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diamondshield_central.dto.reader.ReaderCreateRequest;
import ru.diamondshield_central.dto.reader.ReaderResponse;
import ru.diamondshield_central.dto.reader.ReaderUpdateRequest;
import ru.diamondshield_central.entity.ControllerDevice;
import ru.diamondshield_central.entity.Reader;
import ru.diamondshield_central.exception.EntityNotFoundException;
import ru.diamondshield_central.repository.ControllerDeviceRepository;
import ru.diamondshield_central.repository.ReaderRepository;

import java.util.UUID;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final ControllerDeviceRepository controllerDeviceRepository;
    private final AuditService auditService;

    public ReaderService(ReaderRepository readerRepository,
                         ControllerDeviceRepository controllerDeviceRepository,
                         AuditService auditService) {
        this.readerRepository = readerRepository;
        this.controllerDeviceRepository = controllerDeviceRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<ReaderResponse> getAll(UUID controllerId, Pageable pageable) {
        if (controllerId != null) {
            return readerRepository.findByControllerId(controllerId, pageable)
                    .map(ReaderResponse::fromEntity);
        }

        return readerRepository.findAll(pageable)
                .map(ReaderResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public ReaderResponse getById(UUID id) {
        return ReaderResponse.fromEntity(findEntity(id));
    }

    @Transactional
    public ReaderResponse create(ReaderCreateRequest request, HttpServletRequest httpRequest) {
        ControllerDevice controller = controllerDeviceRepository.findById(request.getControllerId())
                .orElseThrow(() -> new EntityNotFoundException("Controller not found"));

        Reader reader = new Reader();
        reader.setController(controller);
        reader.setName(request.getName());
        reader.setReaderType(request.getReaderType());
        reader.setDirection(request.getDirection());
        reader.setStatus("offline");

        Reader saved = readerRepository.save(reader);

        auditService.log("READER_CREATED", "readers", saved.getId(), null, saved, httpRequest);

        return ReaderResponse.fromEntity(saved);
    }

    @Transactional
    public ReaderResponse update(UUID id,
                                 ReaderUpdateRequest request,
                                 HttpServletRequest httpRequest) {
        Reader reader = findEntity(id);
        Reader oldValue = copy(reader);

        reader.setName(request.getName());
        reader.setReaderType(request.getReaderType());
        reader.setDirection(request.getDirection());

        if (request.getStatus() != null) {
            reader.setStatus(request.getStatus());
        }

        Reader saved = readerRepository.save(reader);

        auditService.log("READER_UPDATED", "readers", saved.getId(), oldValue, saved, httpRequest);

        return ReaderResponse.fromEntity(saved);
    }

    @Transactional
    public void setOffline(UUID id, HttpServletRequest httpRequest) {
        Reader reader = findEntity(id);
        Reader oldValue = copy(reader);

        reader.setStatus("offline");
        readerRepository.save(reader);

        auditService.log("READER_OFFLINE", "readers", id, oldValue, reader, httpRequest);
    }

    public Reader findEntity(UUID id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
    }

    private Reader copy(Reader source) {
        Reader copy = new Reader();
        copy.setController(source.getController());
        copy.setName(source.getName());
        copy.setReaderType(source.getReaderType());
        copy.setDirection(source.getDirection());
        copy.setStatus(source.getStatus());
        return copy;
    }
}