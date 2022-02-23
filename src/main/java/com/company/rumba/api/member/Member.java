package com.company.rumba.api.member;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class Member {
    @Id
    @SequenceGenerator(
            name = "member_sequence",
            sequenceName = "member_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_sequence"
    )
    @Column(name = "member_id")
    private Long memberId;

    @OneToOne
    private AppUser member;

    @OneToOne
    private Task task;

    @Column(name = "start_date")
    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;
}
