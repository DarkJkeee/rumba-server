package com.company.rumba.auth.service;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import org.javatuples.Pair;

import java.time.LocalDateTime;

public interface AuthService {
    String register(RegistrationRequest request);
    Pair<String, LocalDateTime> login(LoginRequest request);
    String confirmToken(String token);
}
