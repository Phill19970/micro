package com.capstone.appointmentservice.model;

import lombok.*;

import java.util.List;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Data
@ToString(exclude = {"availabilities"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    private String id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String specialization;
    private Integer age;
    private String biography;
    private List<String> skills;

}
