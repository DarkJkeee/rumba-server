package com.company.rumba.api.member;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final EventRepository eventRepository;
    private final UserProvider userProvider;

    public void addMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    event.getMembers().add(userProvider.getCurrentAppUser());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event doesn't exist"
                ));
    }
}
