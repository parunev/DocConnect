package com.parunev.docconnect.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.services.UserService;
import com.parunev.docconnect.utils.DCLogger;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A filter for JWT (JSON Web Token) authentication.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final JwtTokenRepository jwTokenRepository;
    private final ObjectMapper objectMapper;
    private static final DCLogger DC_LOGGER = new DCLogger(JwtFilter.class);
    private static final String CORRELATION_ID = "correlationId";
    private static final String[] HEADERS = {"Authorization", "Bearer "};


    /**
     * Perform JWT-based authentication for incoming requests.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If an error occurs during the servlet filter process.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            MDC.put(CORRELATION_ID, generateCorrelationId());
            DCLogger.setDCLoggerProperties(MDC.get(CORRELATION_ID), request);

            DC_LOGGER.debug("Received request: {} {}",
                    request.getMethod(), request.getRequestURI());

            final String authHeader = request.getHeader(HEADERS[0]);
            final String jwt;
            final String email;

            if (authHeader == null || !authHeader.startsWith(HEADERS[1])) {
                DC_LOGGER.debug("No JWT token found in the request. Proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            email = jwtService.extractEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(email);

                boolean isTokenValid = jwTokenRepository.findByToken(jwt)
                        .map(jwToken -> !jwToken.isExpired() && !jwToken.isRevoked()).orElse(false);

                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                    DC_LOGGER.info("User {} authenticated successfully.",
                            userDetails.getUsername());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                filterChain.doFilter(request, response);
            }
        } catch (SignatureException | ExpiredJwtException exception) {
            DC_LOGGER.error("Authentication failed: {}", exception, exception.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(ApiError.builder()
                    .path(request.getRequestURI())
                    .error(exception.getMessage())
                    .status(HttpStatus.UNAUTHORIZED)
                    .timestamp(LocalDateTime.now())
                    .build()));

        } finally {
            MDC.remove(CORRELATION_ID);
            DCLogger.setDCLoggerProperties(null, null);
        }
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
