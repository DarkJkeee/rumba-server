package com.company.rumba.errors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(CustomErrorException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getType(),
                        ServletUriComponentsBuilder.fromCurrentRequest().build().getPath(),
                        e.getMessage()
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
                        e.getBindingResult().getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
