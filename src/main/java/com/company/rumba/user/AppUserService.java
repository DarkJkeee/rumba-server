package com.company.rumba.user;

import com.company.rumba.auth.email.EmailSender;
import com.company.rumba.auth.token.ConfirmationToken;
import com.company.rumba.auth.token.ConfirmationTokenService;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import com.company.rumba.user.requests.ChangePasswordRequest;
import com.company.rumba.user.requests.ChangeUserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new CustomErrorException(
                        HttpStatus.BAD_REQUEST,
                        ErrorType.EMAIL_NOT_FOUND,
                        String.format(USER_NOT_FOUND, email)
                )
        );
    }

    public void setUserEnabled(String email) {
        appUserRepository.enableAppUser(email);
    }

    public void setNewEmail(String email, String newEmail) {
        appUserRepository.setNewEmail(email, newEmail);
    }

    public String signUpUser(AppUser appUser) {
        var user = appUserRepository.findByEmail(appUser.getEmail());

        if (user.isPresent()) {
            if (!user.get().isEnabled()) {
                return generateConfirmationToken(user.get(), user.get().getEmail());
            }

            log.error(String.format("Email %s has already taken", appUser.getEmail()));
            throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    ErrorType.EMAIL_EXIST,
                    String.format("Email %s has already taken", appUser.getEmail())
            );
        }

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);

        return generateConfirmationToken(appUser, appUser.getEmail());
    }

    public AppUser getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loadUserByUsername(userDetails.getUsername());
    }

    public void changeUser(ChangeUserRequest userRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = loadUserByUsername(userDetails.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        if (!user.getEmail().equals(userRequest.getEmail())) {
            String token = generateConfirmationToken(user, userRequest.getEmail());
            String link = String.format("https://rumba-app.herokuapp.com/auth/confirm?token=%s", token);
            emailSender.send(userRequest.getEmail(), user.getFirstName(), link);
        }

        appUserRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest passwordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = loadUserByUsername(userDetails.getUsername());
        if (!bCryptPasswordEncoder.matches(passwordRequest.getPassword(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(passwordRequest.getPassword()));
            appUserRepository.save(user);
            return;
        }

        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ErrorType.VALIDATION_ERROR, "Passwords are the same");
    }

    private String generateConfirmationToken(AppUser appUser, String email) {
        ConfirmationToken token = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().atZone(ZoneId.systemDefault()),
                LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()),
                email,
                appUser
        );
        confirmationTokenService.saveConfirmationToken(token);
        return token.getToken();
    }
}
