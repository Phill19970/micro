package com.capstone.medicalrecordservice.model;

import lombok.*;

/**
 * @author Javaughn Stephenson
 * @since 15/06/2023
 */

@Data
@ToString(exclude = {"appointments"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private Integer age;
    private String bloodGroup;
    private String religion;
    private String occupation;
    private Character gender;
    private String maritalStatus;
    private String address;
    private String description;

}
