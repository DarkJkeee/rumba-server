package com.company.rumba.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(CustomErrorException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getType(),
                        ServletUriComponentsBuilder.fromCurrentRequest().build().getPath(),
                        List.of(e.getMessage())
                ),
                e.getStatus()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ErrorType.VALIDATION_ERROR,
                        ServletUriComponentsBuilder.fromCurrentRequest().build().getPath(),
                        e.getBindingResult().getFieldErrors()
                                .stream()
                                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                                .toList()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
