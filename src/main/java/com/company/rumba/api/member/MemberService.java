package com.company.rumba.api.member;

import com.company.rumba.api.event.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class MemberService {
    private final EventRepository eventRepository;
    private final UserProvider userProvider;

    public void addMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (event.getMembers().stream().anyMatch(user -> user.getAccountId().equals(userProvider.getCurrentUserID()))) {
                        throw new CustomErrorException(
                                HttpStatus.BAD_REQUEST,
                                ErrorType.MEMBER_ALREADY_EXIST,
                                "The user is already a member of the event"
                        );
                    }
                    event.getMembers().add(userProvider.getCurrentAppUser());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event doesn't exist"
                ));
    }

    public void deleteMember(Long eventId) {
        eventRepository
                .findById(eventId)
                .map(event -> {
                    if (event.getMembers().removeIf(user -> Objects.equals(user.getAccountId(), userProvider.getCurrentUserID()))) {
                        return eventRepository.save(event);
                    } else {
                        throw new CustomErrorException(
                                HttpStatus.NOT_FOUND,
                                ErrorType.MEMBER_NOT_FOUND,
                                "Member doesn't exist"
                        );
                    }
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event doesn't exist"
                ));
    }
}
