package ru.diamondshield_central.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.diamondshield_central.dto.sync.SyncHistoryResponse;
import ru.diamondshield_central.entity.LocalServer;
import ru.diamondshield_central.entity.SyncHistory;
import ru.diamondshield_central.repository.SyncHistoryRepository;

@Service
public class SyncHistoryService {

    private final SyncHistoryRepository syncHistoryRepository;

    public SyncHistoryService(SyncHistoryRepository syncHistoryRepository) {
        this.syncHistoryRepository = syncHistoryRepository;
    }

    public Page<SyncHistoryResponse> getAll(java.util.UUID localServerId,
                                            java.util.UUID objectId,
                                            String status,
                                            Pageable pageable) {
        if (localServerId != null) {
            return syncHistoryRepository.findByLocalServerId(localServerId, pageable)
                    .map(SyncHistoryResponse::fromEntity);
        }

        if (objectId != null) {
            return syncHistoryRepository.findByObjectId(objectId, pageable)
                    .map(SyncHistoryResponse::fromEntity);
        }

        if (status != null) {
            return syncHistoryRepository.findByStatus(status, pageable)
                    .map(SyncHistoryResponse::fromEntity);
        }

        return syncHistoryRepository.findAll(pageable)
                .map(SyncHistoryResponse::fromEntity);
    }

    public SyncHistory start(LocalServer localServer, String syncType) {
        SyncHistory history = new SyncHistory();

        // Стартовая запись создается до обработки payload
        history.setLocalServer(localServer);
        history.setObject(localServer.getObject());
        history.setSyncType(syncType);
        history.setStatus("processing");

        return syncHistoryRepository.save(history);
    }

    public SyncHistory finish(SyncHistory history,
                              int total,
                              int accepted,
                              int skipped,
                              int errors,
                              String errorMessage) {
        history.setTotalItems(total);
        history.setAcceptedItems(accepted);
        history.setSkippedItems(skipped);
        history.setErrorItems(errors);
        history.setErrorMessage(errorMessage);
        history.setCompletedAt(java.time.LocalDateTime.now());

        if (errors == 0 && skipped == 0) {
            history.setStatus("success");
        } else if (accepted > 0) {
            history.setStatus("partial");
        } else {
            history.setStatus("error");
        }

        return syncHistoryRepository.save(history);
    }
}