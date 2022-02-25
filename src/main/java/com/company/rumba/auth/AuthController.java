package com.company.rumba.auth;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import com.company.rumba.auth.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        // TODO: Delete token from response
        responseBody.put("token", authService.register(request));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        var response = authService.login(request);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("created_at", LocalDateTime.now().atZone(ZoneId.systemDefault()));
        responseBody.put("expires_at", response.getValue1().atZone(ZoneId.systemDefault()));
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
