package com.company.rumba.api.task;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

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
    @Size(min = 1, max = 40)
    private String title;

    @NotNull(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Members count is mandatory")
    @Min(value = 1)
    private Integer membersCount;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "Start date is mandatory")
    private ZonedDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "End date is mandatory")
    private ZonedDateTime endDate;
}
