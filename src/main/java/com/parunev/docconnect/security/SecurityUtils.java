package com.parunev.docconnect.security;

import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@NoArgsConstructor(access = AccessLevel.NONE)
public class SecurityUtils {

    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new UserNotFoundException(AuthenticationError
                .builder()
                .error("No authentication presented")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED)
                .build());
    }
}
