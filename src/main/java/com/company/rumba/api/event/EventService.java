package com.company.rumba.api.event;

import com.company.rumba.api.dto.ListEvent;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.utils.UserProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;
    private final UserProvider userProvider;

    public void createEvent(Event event) {
        event.setCreator(userProvider.getCurrentAppUser());
        eventRepository.save(event);
    }

    public void changeEvent(Event newEvent, Long id) {
        var event = eventRepository
                .findById(id)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event does not exist"
                ));
        newEvent.setMembers(event.getMembers());
        newEvent.setCreator(userProvider.getCurrentAppUser());
        newEvent.setEventId(id);
        eventRepository.save(newEvent);
    }

    public List<ListEvent> getCreatedEvents() {
        return eventRepository
                .findAllCreatedBy(userProvider.getCurrentUserID())
                .stream()
                .map(event -> modelMapper.map(event, ListEvent.class))
                .collect(Collectors.toList());
    }

    public List<ListEvent> getParticipatedEvents() {
        return eventRepository
                .findAllParticipatedBy(userProvider.getCurrentAppUser())
                .stream()
                .map(event -> modelMapper.map(event, ListEvent.class))
                .collect(Collectors.toList());
    }

    public Event getEvent(Long id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event does not exist"
                ));
    }
}
