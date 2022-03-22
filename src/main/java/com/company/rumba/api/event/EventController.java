package com.company.rumba.api.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createEvent(@Valid @RequestBody Event event) {
        eventService.createEvent(event);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEvent(@Valid @RequestBody Event newEvent, @PathVariable Long id) {
        eventService.changeEvent(newEvent, id);
    }

    @GetMapping("/created")
    public ResponseEntity<?> getCreatedEvents() {
        return ResponseEntity.ok(eventService.getCreatedEvents());
    }

    @GetMapping("/participated")
    public ResponseEntity<?> getParticipatedEvents() {
        return ResponseEntity.ok(eventService.getParticipatedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }
}
