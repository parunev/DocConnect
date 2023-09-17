package com.parunev.docconnect.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parunev.docconnect.security.jwt.JwtFilter;
import com.parunev.docconnect.security.jwt.JwtLogout;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.security.payload.LogoutResponse;
import com.parunev.docconnect.utils.DCLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtLogout jwtLogout;
    private final ObjectMapper objectMapper;
    private final DCLogger dcLogger = new DCLogger(SecurityConfig.class);

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");

                            HandlerMethod method;
                            PreAuthorize preAuthorize;

                            if (request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler") != null) {
                                method = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
                                preAuthorize = method.getMethodAnnotation(PreAuthorize.class);
                            } else {
                                preAuthorize = null;
                            }

                            String message = preAuthorize == null ?
                                    "Something went wrong! Please contact the administrator." :
                                    "Authorization condition not met: " + preAuthorize.value();

                            response.getWriter().write(objectMapper.writeValueAsString(
                                    ApiError.builder()
                                            .path(request.getRequestURI())
                                            .error(message)
                                            .timestamp(LocalDateTime.now())
                                            .status(HttpStatus.UNAUTHORIZED)
                                            .build()
                            ));
                        }))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/v1/countries/**").permitAll()
                                .requestMatchers("/api/v1/cities/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers(
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "swagger-ui.html",
                                        "/").permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(jwtLogout)
                        .logoutSuccessHandler(((request, response, authentication)
                                -> {
                            dcLogger.info("User successfully logged out");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    LogoutResponse.builder()
                                            .path(request.getRequestURI())
                                            .message("User successfully logged out")
                                            .timestamp(LocalDateTime.now())
                                            .status(HttpStatus.UNAUTHORIZED)
                                            .build()
                            ));
                            SecurityContextHolder.clearContext();
                        })))
                .build();
    }

}
