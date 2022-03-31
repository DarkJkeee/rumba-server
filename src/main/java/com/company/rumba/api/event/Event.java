package com.company.rumba.api.event;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    @Column(name = "event_id")
    private Long eventId;

    @NotNull(message = "Title is mandatory")
    @Size(min = 1, max = 40)
    private String title;

    @NotNull(message = "Description is mandatory")
    private String description;

    @Column(name = "is_online")
    @NotNull(message = "The format is mandatory")
    private Boolean isOnline;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "is_rescheduled")
    private Boolean isRescheduled = false;

    @Column(name = "place_name")
    private String placeName;

    private Float latitude;
    private Float longitude;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "Start date is mandatory")
    private ZonedDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "End date is mandatory")
    private ZonedDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<@Valid Task> tasks = new ArrayList<>();

    @ManyToMany
    private List<AppUser> members = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private AppUser creator;
}