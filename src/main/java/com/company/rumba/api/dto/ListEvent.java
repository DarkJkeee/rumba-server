package com.company.rumba.api.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ListEvent {
    private Long eventId;
    private String title;
    private String description;
    private Boolean isOnline;
    private Boolean isCancelled;
    private Boolean isRescheduled;
    private Boolean isActionsRequired;
    private Float latitude;
    private Float longitude;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
