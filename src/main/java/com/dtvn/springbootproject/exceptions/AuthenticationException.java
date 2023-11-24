package com.dtvn.springbootproject.exceptions;

public class AuthenticationException extends RuntimeException {
    private final int errorCode;

    public AuthenticationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}