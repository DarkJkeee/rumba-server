package com.company.rumba.auth;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import com.company.rumba.auth.service.AuthService;
import com.company.rumba.support.DateFormatter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
public class AuthController {
    private final DateFormatter dateFormatter;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", authService.register(request));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        var response = authService.login(request);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("created_at", dateFormatter.dateToString(LocalDateTime.now()));
        responseBody.put("expires_at", dateFormatter.dateToString(response.getValue1()));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", response.getValue0());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseBody);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", authService.confirmToken(token));
        return ResponseEntity.ok(responseBody);
    }
}
