package com.company.rumba.api.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createEvent(@Valid @RequestBody Event event) {
        eventService.createEvent(event);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCreatedEvents() {
        return ResponseEntity.ok(eventService.getCreatedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }
}
