package com.appointmentservice.feign;
import com.appointmentservice.model.DoctorCalendarResponseModel;
import com.appointmentservice.model.DoctorProfileResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
@FeignClient(name = "HEALTHCARE-PROVIDER-SERVICE")
public interface DoctorFeign {
    @GetMapping("/doctors")
     List<DoctorProfileResponseModel> getAllDoctors(@RequestHeader("token") String token);// getting all doctors.
    @PostMapping(value = "/doctors", consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveDoctor(@RequestBody DoctorProfileResponseModel doctorsAvailability);
}