package com.capstone.notificationservice.model;

import lombok.*;

import java.util.List;

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
