package com.capstone.appointmentservice.client;

import com.capstone.appointmentservice.model.MedicalRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
@FeignClient(name = "medical-record-service")
public interface MedicalRecordClient {

    @GetMapping("/api/v1/medical-record/{appointmentId}")
    public MedicalRecord getMedicalRecords(@PathVariable UUID appointmentId);

}
