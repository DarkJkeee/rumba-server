package com.company.rumba.auth;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import com.company.rumba.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/registration")
    public String register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String register(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }
}
