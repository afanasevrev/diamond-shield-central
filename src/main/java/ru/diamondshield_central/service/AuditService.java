package ru.diamondshield_central.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.diamondshield_central.entity.AuditLog;
import ru.diamondshield_central.entity.SystemUser;
import ru.diamondshield_central.repository.AuditLogRepository;
import ru.diamondshield_central.security.CustomUserDetails;

import java.util.UUID;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditService(AuditLogRepository auditLogRepository,
                        ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    public void log(String actionType,
                    String entityType,
                    UUID entityId,
                    Object oldValue,
                    Object newValue,
                    HttpServletRequest request) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setActionType(actionType);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setOldValue(oldValue == null ? null : objectMapper.writeValueAsString(oldValue));
            auditLog.setNewValue(newValue == null ? null : objectMapper.writeValueAsString(newValue));
            auditLog.setIpAddress(resolveIp(request));

            SystemUser currentUser = getCurrentUserOrNull();
            auditLog.setSystemUser(currentUser);

            auditLogRepository.save(auditLog);
        } catch (Exception ignored) {
            /*
             * Аудит не должен ломать основную бизнес-операцию.
             * На следующих этапах можно добавить отдельный error-лог.
             */
        }
    }

    private SystemUser getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    private String resolveIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}