package com.unibuc.main.exception;

public class MedicalRecordNotFoundException extends RuntimeException{

    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
