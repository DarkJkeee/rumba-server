package com.company.rumba.support;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public String dateToString(LocalDateTime date) {
        return dateFormatter.format(date);
    }
}
