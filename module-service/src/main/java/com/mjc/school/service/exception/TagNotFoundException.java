package com.mjc.school.service.exception;

public class TagNotFoundException extends CustomException{

    public static final String CODE = "000004";

    public TagNotFoundException(String message) {
        super(CODE, message);
    }
}
