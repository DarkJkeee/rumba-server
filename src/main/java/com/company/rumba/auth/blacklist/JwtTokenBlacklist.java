package com.company.rumba.auth.blacklist;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
public class JwtTokenBlacklist {

    @Id
    @SequenceGenerator(
            name = "jwt_token_blacklist_sequence",
            sequenceName = "jwt_token_blacklist_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "jwt_token_blacklist_sequence"
    )
    private Long id;

    private String jwt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime expiresAt;

    public JwtTokenBlacklist(String jwt, ZonedDateTime expiresAt) {
        this.jwt = jwt;
        this.expiresAt = expiresAt;
    }
}
