package com.unibuc.main.exception;

public class VaccineAlreadyExistsException extends RuntimeException{

    public VaccineAlreadyExistsException(String message) {
        super(message);
    }
}
