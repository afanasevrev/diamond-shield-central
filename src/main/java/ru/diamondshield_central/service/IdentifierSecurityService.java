package ru.diamondshield_central.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class IdentifierSecurityService {

    public String normalize(String value) {
        if (value == null) {
            return null;
        }

        // Нормализация нужна, чтобы одна и та же карта не добавлялась в разных регистрах или с пробелами
        return value.trim().toUpperCase();
    }

    public String sha256(String normalizedValue) {
        try {
            // SHA-256 используется для хранения идентификаторов без фактического значения
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(normalizedValue.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot calculate identifier hash", ex);
        }
    }

    public String mask(String normalizedValue) {
        if (normalizedValue == null || normalizedValue.isBlank()) {
            return "";
        }

        // Короткие значения маскируем полностью
        if (normalizedValue.length() <= 4) {
            return "****";
        }

        String last = normalizedValue.substring(normalizedValue.length() - 4);

        // Показываем только последние 4 символа
        return "****" + last;
    }
}