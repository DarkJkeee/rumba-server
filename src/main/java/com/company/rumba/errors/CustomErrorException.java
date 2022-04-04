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

    public static CustomErrorException eventNotExistError = new CustomErrorException(
            HttpStatus.NOT_FOUND,
            ErrorType.EVENT_NOT_FOUND,
            "Event doesn't exist"
    );

    public static CustomErrorException taskNotExistError = new CustomErrorException(
            HttpStatus.NOT_FOUND,
            ErrorType.TASK_NOT_FOUND,
            "Task doesn't exist"
    );

    public static CustomErrorException memberNotExistError = new CustomErrorException(
            HttpStatus.NOT_FOUND,
            ErrorType.MEMBER_NOT_FOUND,
            "Member doesn't exist"
    );

    public static CustomErrorException invalidStartAndEndDates = new CustomErrorException(
            HttpStatus.BAD_REQUEST,
            ErrorType.INVALID_DATES,
            "Start date must be less than end date"
    );

    public static CustomErrorException invalidDatesOfTask = new CustomErrorException(
            HttpStatus.BAD_REQUEST,
            ErrorType.INVALID_DATES,
            "Start and end dates of task must be between start and end dates of event"
    );

    public static CustomErrorException forbiddenError(String message) {
        return new CustomErrorException(
                HttpStatus.FORBIDDEN,
                ErrorType.FORBIDDEN,
                message
        );
    }
}