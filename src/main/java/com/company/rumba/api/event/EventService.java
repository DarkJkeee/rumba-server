package com.company.rumba.api.event;

import com.company.rumba.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void createEvent(Event event, AppUser appUser) {
        event.setCreator(appUser);
        eventRepository.save(event);
        log.info("Event saved");
    }

    public List<Event> getCreatedEvents(Long id) {
        return eventRepository.findAllCreatedBy(id);
    }
}
