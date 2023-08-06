package com.patientservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationDetailsModel {
    private String name;
    private String password;
}
