package com.capstone.medicalrecordservice.exception;

public class MedicalRecordExists extends RuntimeException {

    public MedicalRecordExists(String message) {
        super(message);
    }
}
