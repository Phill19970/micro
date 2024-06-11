package com.capstone.notificationservice.controller;

import com.capstone.notificationservice.dto.v1.ExceptionDTO;
import com.capstone.notificationservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;

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

}
