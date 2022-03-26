package com.company.rumba.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public AppUser getUser() {
        return appUserService.getCurrentUser();
    }
}
