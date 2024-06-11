package com.capstone.notificationservice.client;

import com.capstone.notificationservice.model.Doctor;
import com.capstone.notificationservice.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/patients/{patientId}")
    public Patient getPatient(@PathVariable String patientId);

    @GetMapping("/api/v1/doctors/{doctorId}")
    public Doctor getDoctor(@PathVariable String doctorId);

}
