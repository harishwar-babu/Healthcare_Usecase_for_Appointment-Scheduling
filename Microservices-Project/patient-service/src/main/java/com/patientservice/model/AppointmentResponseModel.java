package com.patientservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseModel {
    private String status;
    private Integer doctorId;
    private String patientUsername;
    private String reason;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
