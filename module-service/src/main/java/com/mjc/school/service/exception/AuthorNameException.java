package com.mjc.school.service.exception;

public class AuthorNameException extends CustomException {

    public static final String CODE = "000013";

    public AuthorNameException(String message) {
        super(CODE, message);
    }
}