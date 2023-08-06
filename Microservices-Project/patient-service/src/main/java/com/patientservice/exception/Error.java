package com.patientservice.exception;
import lombok.Data;

@Data
public class Error<T> {
    private T message;
}
