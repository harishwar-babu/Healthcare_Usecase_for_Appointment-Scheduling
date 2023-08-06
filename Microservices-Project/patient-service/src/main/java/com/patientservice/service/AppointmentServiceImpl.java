package com.patientservice.service;

import com.patientservice.feign.AppointmentFeign;
import com.patientservice.model.AppointmentResponseModel;
import com.patientservice.model.ScheduleDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService{
    @Autowired
    private AppointmentFeign appointmentFeign;

    @Override
    public ResponseEntity<AppointmentResponseModel> appointmentDetails(ScheduleDetailsModel scheduleDetailsModel,String token) {
        return appointmentFeign.message(scheduleDetailsModel,token);
    }

}
