package com.patientservice.response;
import com.patientservice.exception.Error;
import lombok.Data;
@Data
public class Response<T> {
    private String status;
    private T data;
    private Error error;
}
