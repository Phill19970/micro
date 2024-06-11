package com.capstone.expenseservice.client;

import com.capstone.expenseservice.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Javaughn Stephenson
 * @since 02/08/2023
 */

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/patients/{patientId}")
    Patient getPatient(@PathVariable String patientId);

}
