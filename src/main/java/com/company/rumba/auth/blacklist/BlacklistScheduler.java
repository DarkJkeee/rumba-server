package com.company.rumba.auth.blacklist;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@AllArgsConstructor
public class BlacklistScheduler {
    private JwtTokenBlacklistRepository jwtTokenBlacklistRepository;

    @Scheduled(cron = "0 0 12 * * ?")
    public void deleteExpiredTokens() {
        jwtTokenBlacklistRepository.deleteAllByExpiresAtIsLessThanEqual(ZonedDateTime.now());
    }
}
