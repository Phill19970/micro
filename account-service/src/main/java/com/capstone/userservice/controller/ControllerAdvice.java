package com.capstone.userservice.controller;

import com.capstone.userservice.dto.v1.ExceptionDTO;
import com.capstone.userservice.exception.ResourceExistsException;
import com.capstone.userservice.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO resourceNotFound(ResourceNotFoundException exception) {
        return ExceptionDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Resource was not found")
                .details(exception.getMessage())
                .timestamp(LocalTime.now())
                .build();
    }

    @ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDTO resourceExistsException(ResourceExistsException exception) {
        return ExceptionDTO.builder()
                .status(HttpStatus.CONFLICT)
                .message("Resource already exists")
                .details(exception.getMessage())
                .timestamp(LocalTime.now())
                .build();
    }

    //For validation error handling
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleValidationExceptions(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return ExceptionDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation Error")
                .details(errors.toString())
                .timestamp(LocalTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleMethodArgumentExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });


        return ExceptionDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation Error")
                .details(errors.toString())
                .timestamp(LocalTime.now())
                .build();
    }

}
