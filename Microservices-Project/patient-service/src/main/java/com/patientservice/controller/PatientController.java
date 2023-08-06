package com.patientservice.controller;
import com.patientservice.dto.SignUpDTO;
import com.patientservice.model.AppointmentResponseModel;
import com.patientservice.model.AuthenticationDetailsModel;
import com.patientservice.model.ScheduleDetailsModel;
import com.patientservice.model.SignUpModel;
import com.patientservice.response.Response;
import com.patientservice.service.AppointmentService;
import com.patientservice.service.SignUpService;
import com.patientservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private SignUpService service;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil util;
    @PostMapping("/signup")
    public ResponseEntity<Response<SignUpDTO>> signup(@RequestBody SignUpModel signUpModel,@RequestHeader(value = "token",required = false)String token){
        if(token!=null)
            util.verify(token);
        return service.signUp(signUpModel);
    }
    // for scheduling appointments
    @PostMapping("/schedule-appointments")
    public ResponseEntity<AppointmentResponseModel> message(@RequestBody ScheduleDetailsModel appointmentDetails,@RequestHeader(value = "token",required = false)String token){
        if(token!=null) {
            System.out.println("SUCCESS");
            util.verify(token);
        }
        return appointmentService.appointmentDetails(appointmentDetails,token);
    }
    // for authentication service;
    @PostMapping("/check-patient-exists")
    public Boolean checkPatientExists(@RequestBody AuthenticationDetailsModel authenticationDetails,@RequestHeader(value = "token",required = false)String token){
//        if(token!=null) {
//            util.verify(token);
//        }
        return service.checkUserExists(authenticationDetails.getName(),authenticationDetails.getPassword());
    }
}