package com.company.rumba.api.member;

import com.company.rumba.api.task.Task;
import com.company.rumba.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByTask(Task task);
    Optional<Member> findByTaskAndMember(Task task, AppUser member);
}
