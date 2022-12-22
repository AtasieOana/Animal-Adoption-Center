package com.unibuc.main.exception;

public class AnimalAlreadyAdoptedException extends RuntimeException{

    public AnimalAlreadyAdoptedException(String message) {
        super(message);
    }
}
