package com.company.rumba.user;

import com.company.rumba.api.event.EventService;
import com.company.rumba.user.requests.ChangePasswordRequest;
import com.company.rumba.user.requests.ChangeUserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class AppUserController {
    private final AppUserService appUserService;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<?> getUser() {
        var user = appUserService.getCurrentUser();
        return ResponseEntity.ok(
                new AppUserWithCount(
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        eventService.countEventsHours(user)
                )
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeUser(@RequestBody ChangeUserRequest userRequest) {
        appUserService.changeUser(userRequest);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @RequestBody @Valid ChangePasswordRequest passwordRequest,
            @RequestHeader Map<String, String> headers
    ) {
        appUserService.changePassword(passwordRequest, headers.get("authorization"));
    }

    @Data
    @AllArgsConstructor
    private static class AppUserWithCount {
        private String email;
        private String firstName;
        private String lastName;
        private Long hoursInEvents;
    }
}