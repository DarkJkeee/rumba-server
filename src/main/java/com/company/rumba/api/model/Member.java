package com.company.rumba.api.model;

import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class Member {
    @Id
    private Long memberId;

    @OneToOne
    private AppUser user;
    @OneToOne
    private Task task;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
