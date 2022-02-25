package com.company.rumba.api.event;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
public class Event {
    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )
    private Long eventId;

    @Column(length = 40)
    @NotNull(message = "Title is mandatory")
    private String title;

    private String description;

    @Column(name = "is_online")
    @NotNull(message = "The format is mandatory")
    private Boolean isOnline;

    // TODO: Can add methods to compute properties.
    private Boolean isCancelled;
    private Boolean rescheduled;

    private Float latitude;
    private Float longitude;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "Start date is mandatory")
    private ZonedDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "End date is mandatory")
    private ZonedDateTime endDate;

    @OneToMany
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name="creator_id")
    private AppUser creator;
}