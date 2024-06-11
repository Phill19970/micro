package com.capstone.userservice.mapper;

import com.capstone.userservice.dto.v1.PatientSignUp;
import com.capstone.userservice.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    Patient toEntity(PatientSignUp patientSignUp);

    PatientSignUp toDTO(Patient patient);
}
