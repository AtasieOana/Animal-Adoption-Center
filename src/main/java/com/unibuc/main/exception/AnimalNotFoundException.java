package com.unibuc.main.exception;

public class AnimalNotFoundException extends RuntimeException{

    public AnimalNotFoundException(String message) {
        super(message);
    }
}
