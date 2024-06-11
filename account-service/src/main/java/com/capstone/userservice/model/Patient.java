package com.capstone.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@ToString(exclude = {"appointments"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Patient {
    @Id
    private String id;

    @NotBlank(message = "Username is required")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Age is required")
    private Integer age;

    private String bloodGroup;

    private String religion;

    private String occupation;

    @NotNull(message = "Gender is required")
    private Character gender;

    private String maritalStatus;

    @NotBlank(message = "Address is required")
    private String address;

    @Size(max = 100, message = "Description should be at most 100 characters")
    private String description;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}
