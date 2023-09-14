package br.exitus.api.infra.exception;

public class AuthException extends RuntimeException{
    public AuthException(String message) {
        super(message);
    }
}
