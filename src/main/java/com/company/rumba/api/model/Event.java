package com.company.rumba.api.model;

import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
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
    @Column(length = 40, nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    private Boolean isOnline;
    private Float latitude;
    private Float longitude;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;

    @OneToMany
    private List<Task> tasks;
    @ManyToOne
    @JoinColumn(name="creator_id")
    private AppUser creator;
}