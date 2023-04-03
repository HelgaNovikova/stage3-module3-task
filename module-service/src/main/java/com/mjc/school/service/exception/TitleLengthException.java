package com.mjc.school.service.exception;

public class TitleLengthException extends CustomException {

    public static final String CODE = "000012";

    public TitleLengthException(String message) {
        super(CODE, message);
    }
}
