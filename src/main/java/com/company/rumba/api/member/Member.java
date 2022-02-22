package com.company.rumba.api.member;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import lombok.Data;

import javax.persistence.Column;
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
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
}
