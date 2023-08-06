package com.patientservice.service;
import com.patientservice.dto.SignUpDTO;
import com.patientservice.model.SignUpModel;
import com.patientservice.repository.SignUpRepository;
import com.patientservice.response.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class SignUpServiceImpl implements SignUpService{
    private static final String SUCCESS_MESSAGE = "Success";
    @Autowired
    private SignUpRepository signUpRepository;

    @Autowired
    private Response<SignUpDTO> response;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<Response<SignUpDTO>> signUp(SignUpModel signUpModel) {
        signUpRepository.save(signUpModel);
        SignUpDTO signUpDTO = modelMapper.map(signUpModel,SignUpDTO.class);
        response.setStatus(SUCCESS_MESSAGE);
        response.setData(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @Override
    public Boolean checkUserExists(String userName, String password) {
        return signUpRepository.existsByUsernameAndPassword(userName,password);
    }
}