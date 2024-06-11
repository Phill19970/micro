package com.capstone.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@ToString(exclude = {"availabilities"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Doctor {

    @Id
    private String id;

    @NotBlank(message = "Username is required")
    private String name;

    @Email(message = "Invalid email address")
    private String email;

    @JsonIgnore
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String specialization;

    @NotNull(message = "Age is required")
    private Integer age;

    private String biography;


    @NotNull(message = "Skills list is required")
    @Size(min = 1, message = "Doctor's skills list must have at least one skill")
    private List<String> skills;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Availability> availabilities;


}
