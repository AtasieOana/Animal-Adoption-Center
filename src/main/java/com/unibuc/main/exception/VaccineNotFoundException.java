package com.unibuc.main.exception;

public class VaccineNotFoundException extends RuntimeException{

    public VaccineNotFoundException(String message) {
        super(message);
    }
}
