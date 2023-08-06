package com.patientservice.configuration;
import com.patientservice.response.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Configurationclass {
    @Bean
    @Scope("prototype")
    public Response<?> response(){
        return new Response<>();
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
