package com.company.rumba.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListEvent {
    private String title;
    private String description;
    private Boolean isOnline;
    private Float latitude;
    private Float longitude;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
