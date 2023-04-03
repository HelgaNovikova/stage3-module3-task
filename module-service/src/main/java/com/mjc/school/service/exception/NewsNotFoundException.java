package com.mjc.school.service.exception;

public class NewsNotFoundException extends CustomException {

    public static final String CODE = "000001";

    public NewsNotFoundException(String message) {
        super(CODE, message);
    }
}
