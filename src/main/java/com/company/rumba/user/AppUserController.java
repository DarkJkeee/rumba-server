package com.company.rumba.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public AppUser getUser() {
        return appUserService.getCurrentUser();
    }

    @PutMapping
    public void changeUser(@RequestBody ChangeUserRequest userRequest) {
        appUserService.changeUser(userRequest);
    }
}
