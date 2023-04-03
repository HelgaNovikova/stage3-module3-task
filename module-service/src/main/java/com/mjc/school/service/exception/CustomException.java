package com.mjc.school.service.exception;

public abstract class CustomException extends RuntimeException {
    public CustomException(String code, String message) {
        super("ERROR_CODE: " + code + " ERROR_MESSAGE: " + message);
    }
}