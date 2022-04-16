package com.company.rumba.auth;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import com.company.rumba.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@RequestBody RegistrationRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var response = authService.login(request);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("created_at", ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        responseBody.put("expires_at", response.getValue1().truncatedTo(ChronoUnit.SECONDS));
        responseBody.put("token", response.getValue0());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", authService.confirmToken(token));
        return ResponseEntity.ok(responseBody);
    }
}
