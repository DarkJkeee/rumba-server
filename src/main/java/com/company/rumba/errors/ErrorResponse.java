package com.company.rumba.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private String type;
    private String path;
    private String message;
    private List<String> messages;

    public ErrorResponse(ErrorType type, String path) {
        timestamp = new Date();
        this.type = type.name();
        this.path = path;
    }

    public ErrorResponse(ErrorType type, String path, String message) {
        this(type, path);
        this.message = message;
        this.messages = null;
    }

    public ErrorResponse(ErrorType type, String path, List<String> messages) {
        this(type, path);
        this.messages = messages;
        this.message = null;
    }
}
