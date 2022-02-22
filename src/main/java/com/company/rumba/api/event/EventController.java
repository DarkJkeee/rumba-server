package com.company.rumba.api.event;

import com.company.rumba.support.UserProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    private final UserProvider userProvider;

    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createEvent(@RequestBody Event event) {
        eventService.createEvent(event, userProvider.getCurrentAppUser());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCreatedEvent() {
        return ResponseEntity.ok(eventService.getCreatedEvents(userProvider.getCurrentUserID()));
    }
}
