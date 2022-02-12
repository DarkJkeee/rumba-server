package com.company.rumba.api.controller;

import com.company.rumba.api.model.Event;
import com.company.rumba.api.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createEvent(@RequestBody Event event) {
        eventService.createEvent(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }
}
