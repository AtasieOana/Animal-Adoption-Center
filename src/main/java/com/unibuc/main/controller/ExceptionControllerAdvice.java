package com.unibuc.main.controller;

import com.unibuc.main.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({EmployeeNotFoundException.class, EntityNotFoundException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<String> handleControllerExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}