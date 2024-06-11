package com.capstone.medicalrecordservice.exception;

/**
 * @author Javaughn Stephenson
 * @since 03/08/2023
 */

public class MedicalRecordExists extends RuntimeException {

    public MedicalRecordExists(String message) {
        super(message);
    }
}
