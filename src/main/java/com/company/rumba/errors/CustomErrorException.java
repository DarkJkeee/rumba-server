package com.company.rumba.errors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class CustomErrorException extends RuntimeException {
    private HttpStatus status;
    private ErrorType type;
    private String message;

    public CustomErrorException(HttpStatus status, ErrorType type, String message) {
        this.status = status;
        this.type = type;
        this.message = message;
    }
}