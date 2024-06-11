package com.capstone.userservice.mapper;


import com.capstone.userservice.dto.v1.DoctorDTO;
import com.capstone.userservice.dto.v1.DoctorSignUp;
import com.capstone.userservice.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    Doctor toEntity(DoctorDTO doctorDTO);
    DoctorDTO toDTO(Doctor doctor);
    Doctor toEntity(DoctorSignUp doctorSignUp);


}
