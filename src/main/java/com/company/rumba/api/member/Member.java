package com.company.rumba.api.member;

import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

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
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private AppUser member;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "Start date is mandatory")
    private ZonedDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull(message = "End date is mandatory")
    private ZonedDateTime endDate;
}
