package com.company.rumba.api.task;

import com.company.rumba.api.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "members_count")
    private Integer membersCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "Start date is mandatory")
    private ZonedDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "End date is mandatory")
    private ZonedDateTime endDate;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @JsonIgnore
    private ZonedDateTime createdAt;

    @Column(name = "edited_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @JsonIgnore
    private ZonedDateTime editedAt;
}
