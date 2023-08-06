package com.appointmentservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
    //on success sending the appointment response back to the patient service
    private String status;
    private Integer doctorId;
    private String patientUsername;
    private String reason;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
