package com.company.rumba.api.event;

import com.company.rumba.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.creator.accountId = :id")
    List<Event> findAllCreatedBy(Long id);

    @Query("SELECT e FROM Event e WHERE :appUser member of e.members")
    List<Event> findAllParticipatedBy(AppUser appUser);
}
