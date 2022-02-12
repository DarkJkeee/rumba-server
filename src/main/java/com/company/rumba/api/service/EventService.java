package com.company.rumba.api.service;

import com.company.rumba.api.model.Event;
import com.company.rumba.api.repo.EventRepository;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.errors.PathProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void createEvent(Event event) {
        log.info("before " + event.toString());
        log.info(eventRepository.save(event).toString());
    }

    public Event getEvent(Long id) {
        log.info("Sent id: " + id);
        return eventRepository.findById(id).orElseThrow(() -> new CustomErrorException(
                HttpStatus.BAD_REQUEST,
                ErrorType.ACCOUNT_NOT_CONFIRMED,
                PathProvider.getCurrentPath(),
                "bad request ai ai ai"
        ));
    }
}
