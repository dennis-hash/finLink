package com.example.MCrypto.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
}
