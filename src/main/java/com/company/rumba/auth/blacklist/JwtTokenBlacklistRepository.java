package com.company.rumba.auth.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface JwtTokenBlacklistRepository extends JpaRepository<JwtTokenBlacklist, Long> {
    Optional<JwtTokenBlacklist> findByJwt(String jwt);
    void deleteAllByExpiresAtIsLessThanEqual(ZonedDateTime now);
}
