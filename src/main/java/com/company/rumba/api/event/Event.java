package com.company.rumba.api.event;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    private Float latitude;
    private Float longitude;

    @Column(name = "start_date")
    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;

    @OneToMany
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name="creator_id")
    private AppUser creator;
}