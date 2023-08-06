package com.patientservice.service;

import com.patientservice.model.AppointmentResponseModel;
import com.patientservice.model.ScheduleDetailsModel;
import org.springframework.http.ResponseEntity;

public interface AppointmentService {
    ResponseEntity<AppointmentResponseModel> appointmentDetails(ScheduleDetailsModel scheduleDetailsModel,String token);
}
