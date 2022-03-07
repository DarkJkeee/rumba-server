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

    public Event createEvent(Event event) {
        event.setCreator(userProvider.getCurrentAppUser());
        Event savedEvent = eventRepository.save(event);
        log.info("Event saved");
        return savedEvent;
    }

    public List<ListEvent> getCreatedEvents() {
        return eventRepository
                .findAllCreatedBy(userProvider.getCurrentUserID())
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

    public Event changeEvent(Event newEvent, Long id) {
        return eventRepository
                .findById(id)
                .map(event -> {
                    if (newEvent.getTitle() != null) event.setTitle(newEvent.getTitle());
                    if (newEvent.getDescription() != null) event.setDescription(newEvent.getDescription());
                    if (newEvent.getIsOnline() != null) event.setIsOnline(newEvent.getIsOnline());
                    if (newEvent.getStartDate() != null) event.setStartDate(newEvent.getStartDate());
                    if (newEvent.getEndDate() != null) event.setEndDate(newEvent.getEndDate());
                    if (newEvent.getLatitude() != null) event.setLatitude(newEvent.getLatitude());
                    if (newEvent.getLongitude() != null) event.setLongitude(newEvent.getLongitude());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND,
                        ErrorType.EVENT_NOT_FOUND,
                        "Event doesn't exist"
                ));
    }
}
