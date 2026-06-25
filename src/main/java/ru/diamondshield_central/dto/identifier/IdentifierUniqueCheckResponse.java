package ru.diamondshield_central.dto.identifier;

public class IdentifierUniqueCheckResponse {

    private boolean unique;
    private String message;

    public IdentifierUniqueCheckResponse() {
    }

    public IdentifierUniqueCheckResponse(boolean unique, String message) {
        // true означает, что идентификатор можно добавить
        this.unique = unique;
        this.message = message;
    }

    public boolean isUnique() {
        return unique;
    }

    public String getMessage() {
        return message;
    }
}