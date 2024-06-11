package com.capstone.userservice.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDTO implements Serializable {
    private String id;

    @NotBlank(message = "Username is required")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;

    @Size(max = 500, message = "Biography should be at most 500 characters")
    private String biography;
}
