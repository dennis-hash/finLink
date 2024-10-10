package com.example.MCrypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    @JsonIgnore
    protected LocalDateTime timeStamp;
    protected int statusCode;
    @JsonIgnore
    protected HttpStatus status;
    protected String message;

    protected Object data;
}
