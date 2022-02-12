package com.company.rumba.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public String dateToString(LocalDateTime date) {
        return dateFormatter.format(date);
    }
}
