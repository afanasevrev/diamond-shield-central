package ru.diamondshield_central.exception;

public class LocalServerAuthenticationException extends RuntimeException {
    public LocalServerAuthenticationException(String message) {
        super(message);
    }
}
