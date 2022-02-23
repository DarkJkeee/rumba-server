package com.company.rumba.api.task;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    @Column(name = "task_id")
    private Long taskId;

    @NotNull(message = "Title is mandatory")
    private String title;

    private String description;

    @Column(name = "start_date")
    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;
}
