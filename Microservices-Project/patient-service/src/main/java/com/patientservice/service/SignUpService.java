package com.patientservice.service;
import com.patientservice.dto.SignUpDTO;
import com.patientservice.model.SignUpModel;
import com.patientservice.response.Response;
import org.springframework.http.ResponseEntity;
public interface SignUpService {
    ResponseEntity<Response<SignUpDTO>> signUp(SignUpModel signUpModel);
    Boolean checkUserExists(String userName,String password);
}
