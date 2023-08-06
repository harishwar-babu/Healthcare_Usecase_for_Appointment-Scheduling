package com.appointmentservice.controller;
import com.appointmentservice.dto.AppointmentDetailsDto;
import com.appointmentservice.dto.AppointmentResponseDto;
import com.appointmentservice.feign.DoctorFeign;
import com.appointmentservice.service.AppointmentService;
import com.appointmentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorFeign feign;

    @Autowired
    private JwtUtil util;

    @PostMapping("/schedule-appointment")
    public ResponseEntity<AppointmentResponseDto> scheduleAppointment(@RequestBody AppointmentDetailsDto appointmentDetails,@RequestHeader(value = "token",required = false)String token){
        if(token!=null){
            util.verify(token);}
        return appointmentService.appointmentBooking(appointmentDetails,token);
    }

}
