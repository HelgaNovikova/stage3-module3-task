package com.mjc.school.service.exception;

public class ContentLengthException extends CustomException {

    public static final String CODE = "000012";

    public ContentLengthException(String message) {
        super(CODE, message);
    }
}
