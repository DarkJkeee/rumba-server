package com.company.rumba.auth.service;

import com.company.rumba.auth.jwt.JwtProvider;
import com.company.rumba.auth.request.LoginRequest;
import com.company.rumba.auth.request.RegistrationRequest;
import com.company.rumba.auth.token.ConfirmationTokenService;
import com.company.rumba.auth.email.EmailSender;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.user.AppUser;
import com.company.rumba.user.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String register(RegistrationRequest request) {
        // TODO: email validation
        String token =  appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String link = String.format("https://rumba-app.herokuapp.com/auth/confirm?token=%s", token);
        emailSender.send(request.getEmail(), request.getFirstName(), link);
        return token;
    }

    @Override
    public Pair<String, ZonedDateTime> login(LoginRequest request) {
        AppUser user = appUserService.loadUserByUsername(request.getEmail());
        if (!user.isEnabled()) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.ACCOUNT_NOT_CONFIRMED,
                    "Account hasn't confirmed yet"
            );
        }
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.INVALID_CREDENTIALS,
                    "Incorrect email or password"
            );
        }

        return jwtProvider.generateToken(user.getEmail());
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        var confirmationToken = confirmationTokenService.getConfirmationToken(token)
                .orElseThrow(() -> {
                    throw new CustomErrorException(
                            HttpStatus.BAD_REQUEST,
                            ErrorType.CONFIRM_TOKEN_NOT_EXIST,
                            "Confirmation token doesn't exist"
                    );
                });
        if (confirmationToken.getConfirmedAt() != null) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.EMAIL_CONFIRMED,
                    "Email has already confirmed"
            );
        }
        if (confirmationToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.CONFIRM_TOKEN_EXPIRED,
                    "Confirmation token has expired"
            );
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.setUserEnabled(confirmationToken.getAppUser().getEmail());
        if (!confirmationToken.getAppUser().getEmail().equals(confirmationToken.getEmail())) {
            appUserService.setNewEmail(confirmationToken.getAppUser().getEmail(), confirmationToken.getEmail());
        }

        return "Account confirmed!";
    }
}
