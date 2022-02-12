package com.company.rumba.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private String type;
    private String path;
    private String message;

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(ErrorType type, String path, String message) {
        this();
        this.type = type.name();
        this.path = path;
        this.message = message;
    }
}
