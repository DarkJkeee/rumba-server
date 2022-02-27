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

    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCreatedEvents() {
        return ResponseEntity.ok(eventService.getCreatedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        return ResponseEntity.ok(eventService.changeEvent(newEvent, id));
    }
}
