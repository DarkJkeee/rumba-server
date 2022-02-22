package com.company.rumba.support;

import com.company.rumba.user.AppUser;
import com.company.rumba.user.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProvider {
    private final AppUserService appUserService;

    public AppUser getCurrentAppUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserService.loadUserByUsername(userDetails.getUsername());
    }

    public Long getCurrentUserID() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserService.loadUserByUsername(userDetails.getUsername()).getAccountId();
    }
}
