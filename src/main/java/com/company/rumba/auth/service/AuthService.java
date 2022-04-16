package com.company.rumba.auth.service;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import org.javatuples.Pair;

import java.time.ZonedDateTime;

public interface AuthService {
    void register(RegistrationRequest request);
    Pair<String, ZonedDateTime> login(LoginRequest request);
    String confirmToken(String token);
}
