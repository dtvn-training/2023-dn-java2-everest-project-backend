package com.dtvn.springbootproject.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorException extends RuntimeException{
    private final int errorCode;
    private List<String> errors;
    public ErrorException(String message,int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public ErrorException(String message,int errorCode,List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = List.of(message);
    }
}
