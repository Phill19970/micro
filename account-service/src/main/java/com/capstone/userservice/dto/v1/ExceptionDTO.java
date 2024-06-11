package com.capstone.userservice.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionDTO {

    private HttpStatus status;
    private String message;
    private String details;
    private LocalTime timestamp;

}