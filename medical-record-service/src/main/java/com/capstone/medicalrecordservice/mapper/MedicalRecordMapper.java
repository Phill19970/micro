package com.capstone.medicalrecordservice.mapper;

import com.capstone.medicalrecordservice.dto.v1.MedicalRecordDTO;
import com.capstone.medicalrecordservice.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Javaughn Stephenson
 * @since 21/06/2023
 */

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecordMapper INSTANCE = Mappers.getMapper(MedicalRecordMapper.class);

    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);



}
