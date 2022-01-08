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
    private Long eventId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany
    private List<Task> tasks;
    @ManyToOne
    @JoinColumn(name="creator_id")
    private AppUser creator;

}
