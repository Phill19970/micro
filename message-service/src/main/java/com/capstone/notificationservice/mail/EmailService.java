package com.capstone.notificationservice.mail;

import com.capstone.notificationservice.client.UserClient;
import com.capstone.notificationservice.config.MqConfig;
import com.capstone.notificationservice.exception.ResourceNotFoundException;
import com.capstone.notificationservice.model.Appointment;
import com.capstone.notificationservice.model.Doctor;
import com.capstone.notificationservice.model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    private final UserClient userClient;

    @RabbitListener(queues = MqConfig.QUEUE)
    public void sendAppointmentEmail(Appointment appointment) {
        log.info("Message Received {}", appointment);

        Doctor doctor = null;
        Patient patient = null;

        try{
            doctor = userClient.getDoctor(appointment.getDoctorId());
            patient = userClient.getPatient(appointment.getPatientId());

        } catch (Exception e) {
            log.error("Patient with id {} or doctor with id {} does not exist.", appointment.getDoctorId(), appointment.getPatientId());
            throw new ResourceNotFoundException("Patient with id " + appointment.getPatientId() + " or doctor with id "+ appointment.getDoctorId() + " does not exist.");
        }


        SimpleMailMessage doctorMail = new SimpleMailMessage();
        doctorMail.setTo(doctor.getEmail());
        doctorMail.setSubject("Scheduled Appointment");
        doctorMail.setText("New appointment schedule with " + patient.getName()
                + " at " + appointment.getAppointmentDate() + " from " + appointment.getStartTime()
                + " to " + appointment.getEndTime());

        SimpleMailMessage patientMail = new SimpleMailMessage();
        patientMail.setTo(patient.getEmail());
        patientMail.setSubject("Scheduled Appointment");
        patientMail.setText("New appointment schedule with " + doctor.getName()
                + " at " + appointment.getAppointmentDate() + " from " + appointment.getStartTime()
                + " to " + appointment.getEndTime());


        mailSender.send(doctorMail);
        mailSender.send(patientMail);

        log.info("Message Sent");

    }

}
