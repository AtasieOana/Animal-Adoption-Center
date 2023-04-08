package com.unibuc.main.controller;

import com.unibuc.main.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({EmployeeNotFoundException.class, CageNotFoundException.class,
            RegisteredVaccineNotFoundException.class,
            VaccineNotFoundException.class, DietNotFoundException.class, ClientNotFoundException.class,
            MedicalRecordNotFoundException.class, AnimalNotFoundException.class,
            EntityNotFoundException.class, SQLIntegrityConstraintViolationException.class,
            RuntimeException.class})
    public ResponseEntity<String> handleNotFoundExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({EmployeeInfoWrongException.class,
            VaccineAlreadyExistsException.class, DietAlreadyExistsException.class,
            ClientAlreadyExistsException.class, AnimalAlreadyAdoptedException.class,
            NoPlaceInCageException.class})
    public ResponseEntity<String> handleAlreadyExistsExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidField(MethodArgumentNotValidException exception) {
        String invalidFields = "Invalid fields: \n"
                + exception.getBindingResult().getFieldErrors().stream()
                .map(e -> "Field: " + e.getField() + ", error: " + e.getDefaultMessage() + ", value: " + e.getRejectedValue())
                .collect(Collectors.joining("\n"));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(invalidFields);
    }
}