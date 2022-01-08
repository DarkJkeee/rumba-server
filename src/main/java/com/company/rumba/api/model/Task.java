package com.company.rumba.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Task {
    @Id
    private Long taskId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
