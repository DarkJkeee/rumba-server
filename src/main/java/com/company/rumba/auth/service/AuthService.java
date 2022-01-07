package com.company.rumba.auth.service;

import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;

public interface AuthService {
    String register(RegistrationRequest request);
    String login(LoginRequest request);
    String confirmToken(String token);
}
