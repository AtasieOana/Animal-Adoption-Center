package com.unibuc.main.exception;

public class RegisteredVaccineNotFoundException extends RuntimeException{

    public RegisteredVaccineNotFoundException(String message) {
        super(message);
    }
}
