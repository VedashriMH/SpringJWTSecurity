package com.ust.Security.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {
        ex.getConstraintViolations().forEach(violation -> {
            System.out.println("Validation error: " + violation.getMessage());
        });
        return ResponseEntity.badRequest().body("Validation error: " + ex.getMessage());
    }
    // Handle invalid arguments in @RequestBody or @Valid for method arguments
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder("Validation error: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessages.append(error.getDefaultMessage()).append(" ");
        });
        return ResponseEntity.badRequest().body(errorMessages.toString());
    }

    // Handle UsernameNotFoundException (invalid login attempt)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(401).body("Authentication failed: " + ex.getMessage());
    }

    // Handle all other general exceptions (unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
    }

    // Handle BindException for incorrect request body binding
    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBindException(BindException ex) {
        StringBuilder errorMessages = new StringBuilder("Binding error: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessages.append(error.getDefaultMessage()).append(" ");
        });
        return ResponseEntity.badRequest().body(errorMessages.toString());
    }
}

