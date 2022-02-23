package com.company.rumba.api.event;

import com.company.rumba.api.dto.ListEvent;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.support.UserProvider;
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
        log.info("Event saved");
    }

    public List<ListEvent> getCreatedEvents() {
        return eventRepository.findAllCreatedBy(userProvider.getCurrentUserID())
                .stream()
                .map(event -> modelMapper.map(event, ListEvent.class))
                .collect(Collectors.toList());
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.BAD_REQUEST,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event does not exist"
                ));
    }
}
