package com.dtvn.springbootproject.exceptions;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException{
    private final int errorCode;
    public ErrorException(String message,int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
