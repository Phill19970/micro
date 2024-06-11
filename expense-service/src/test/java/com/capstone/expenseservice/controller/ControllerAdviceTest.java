package com.capstone.expenseservice.controller;

import com.capstone.expenseservice.dto.v1.ExceptionDTO;
import com.capstone.expenseservice.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    public void testResourceNotFound() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // Act
        ExceptionDTO response = controllerAdvice.resourceNotFound(exception);

        // Assert
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(response.getStatus());
        assertThat("Resource was not found").isEqualTo(response.getMessage());
        assertThat("Resource not found").isEqualTo(response.getDetails());
        assertThat(LocalTime.now().getHour()).isEqualTo(response.getTimestamp().getHour());
    }


    @Test
    public void testHandleValidationExceptions() {
        // Arrange
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);

        Set<ConstraintViolation<?>> violations = Set.of(constraintViolation);
        given(ex.getConstraintViolations()).willReturn(violations);

        given(constraintViolation.getPropertyPath()).willReturn(propertyPath);
        given(constraintViolation.getMessage()).willReturn("Field must not be empty");

        // Custom string representation for the mocked Path object
        given(propertyPath.toString()).willReturn("fieldName");

        // Act
        ExceptionDTO response = controllerAdvice.handleValidationExceptions(ex);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "Field must not be empty");

        assertThat(response.getDetails()).isEqualTo(expectedErrors.toString());
    }

    @Test
    public void testHandleMethodArgumentExceptions() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = mock(FieldError.class);
        BindingResult bindingResult = mock(BindingResult.class);

        given(ex.getBindingResult()).willReturn(bindingResult);
        given(bindingResult.getFieldErrors()).willReturn(List.of(fieldError));

        given(fieldError.getField()).willReturn("fieldName");
        given(fieldError.getDefaultMessage()).willReturn("Field must not be empty");

        // Act
        ExceptionDTO response = controllerAdvice.handleMethodArgumentExceptions(ex);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "Field must not be empty");

        assertThat(response.getDetails()).isEqualTo(expectedErrors.toString());
    }

}