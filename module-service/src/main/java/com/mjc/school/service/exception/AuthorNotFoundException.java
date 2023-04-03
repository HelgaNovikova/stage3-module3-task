package com.mjc.school.service.exception;

public class AuthorNotFoundException extends CustomException {

    public static final String CODE = "000002";

    public AuthorNotFoundException(String message) {
        super(CODE, message);
    }

}