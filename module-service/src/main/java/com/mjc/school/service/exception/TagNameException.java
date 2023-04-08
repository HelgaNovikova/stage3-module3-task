package com.mjc.school.service.exception;

public class TagNameException extends CustomException {

    public static final String CODE = "000023";

    public TagNameException(String message) {
        super(CODE, message);
    }
}
