package com.appointmentservice.configuration;

import com.appointmentservice.dto.AppointmentResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConfigurationClass {
    @Bean
    @Scope("prototype")
    public AppointmentResponseDto appointmentResponseDto(){
        return new AppointmentResponseDto();
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
