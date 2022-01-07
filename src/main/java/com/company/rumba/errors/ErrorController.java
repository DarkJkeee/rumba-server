package com.company.rumba.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(Exception e) {
        CustomErrorException customErrorException = (CustomErrorException) e;

        HttpStatus status = customErrorException.getStatus();
        return new ResponseEntity<>(
                new ErrorResponse(
                        customErrorException.getType(),
                        customErrorException.getPath(),
                        customErrorException.getMessage(),
                        status
                ),
                status
        );
    }
}
