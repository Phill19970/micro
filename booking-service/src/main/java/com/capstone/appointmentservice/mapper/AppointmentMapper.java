package com.capstone.appointmentservice.mapper;

import com.capstone.appointmentservice.DTO.v1.AppointmentDTO;
import com.capstone.appointmentservice.DTO.v1.GetAppointmentDTO;
import com.capstone.appointmentservice.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    AppointmentDTO toDTO(Appointment appointment);
    GetAppointmentDTO toReturnDTO(Appointment appointment);

    Appointment toEntity(AppointmentDTO appointmentDTO);

}
