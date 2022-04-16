package com.company.rumba.auth.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("$(jwt.secret)")
    private String jwtSecret;

    public Pair<String, ZonedDateTime> generateToken(String login) {
        ZonedDateTime expiresAt = ZonedDateTime.now().plusMinutes(30);
        return new Pair<>(
                Jwts.builder()
                        .setSubject(login)
                        .setExpiration(Date.from(expiresAt.toInstant()))
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .compact(),
                expiresAt
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("token expired");
        } catch (Exception ignored) { }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}