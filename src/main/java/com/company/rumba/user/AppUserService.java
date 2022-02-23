package com.company.rumba.user;

import com.company.rumba.auth.token.ConfirmationToken;
import com.company.rumba.auth.token.ConfirmationTokenService;
import com.company.rumba.errors.CustomErrorException;
import com.company.rumba.errors.ErrorType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND = "User with email %s not found";

    private final AppUserRepository appUserRepository;
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

    public String signUpUser(AppUser appUser) {
        var user = appUserRepository.findByEmail(appUser.getEmail());

        if (user.isPresent()) {
            if (!user.get().isEnabled()) {
                return generateConfirmationToken(user.get());
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

        return generateConfirmationToken(appUser);
    }

    private String generateConfirmationToken(AppUser appUser) {
        ConfirmationToken token = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(token);
        return token.getToken();
    }
}
