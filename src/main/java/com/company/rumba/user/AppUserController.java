package com.company.rumba.user;

import com.company.rumba.user.requests.ChangePasswordRequest;
import com.company.rumba.user.requests.ChangeUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
}
