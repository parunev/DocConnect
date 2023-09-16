package com.parunev.docconnect.security.jwt;

import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * A logout handler for JWT (JSON Web Token) based authentication.
 */
@Service
@RequiredArgsConstructor
public class JwtLogout implements LogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;

    /**
     * Perform logout actions for JWT-based authentication.
     *
     * @param request        The HTTP request.
     * @param response       The HTTP response.
     * @param authentication The authentication object.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        jwt = authHeader.substring(7);
        JwtToken storedToken = jwtTokenRepository.findByToken(jwt).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            jwtTokenRepository.save(storedToken);
        }
    }
}
