package com.appointmentservice.service;
import com.appointmentservice.dto.AppointmentDetailsDto;
import com.appointmentservice.dto.AppointmentResponseDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentService {
    int secondsConverter(LocalTime time);
    boolean doctorAvailability(Integer doctorId, LocalDate date, LocalTime startTime, LocalTime endTime);
    ResponseEntity<AppointmentResponseDto> appointmentBooking(AppointmentDetailsDto appointmentDetails,String token);
}
