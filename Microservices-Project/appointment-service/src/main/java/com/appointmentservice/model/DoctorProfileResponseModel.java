package com.appointmentservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileResponseModel {
    private int id;
    private String name;
    private String address;
    private BigInteger phone;
    private String email;
    private LocalDate date;
    private String availability;
    private int healthCareProviderId;
}
