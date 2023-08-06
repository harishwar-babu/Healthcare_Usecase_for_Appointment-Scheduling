package com.patientservice.feign;
import com.patientservice.model.AppointmentResponseModel;
import com.patientservice.model.ScheduleDetailsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "APPOINTMENT-SERVICE",path = "/appointment-service/appointment")
public interface AppointmentFeign {
    @PostMapping("/schedule-appointment")
    public ResponseEntity<AppointmentResponseModel> message(@RequestBody ScheduleDetailsModel appointmentDetails, @RequestHeader(value = "token",required = false) String token);
}
