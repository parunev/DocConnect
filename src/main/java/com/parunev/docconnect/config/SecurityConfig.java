package com.parunev.docconnect.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parunev.docconnect.security.jwt.JwtFilter;
import com.parunev.docconnect.security.jwt.JwtLogout;
import com.parunev.docconnect.security.oauth2.CustomOAuth2UserService;
import com.parunev.docconnect.security.oauth2.HttpCookieRequestRepository;
import com.parunev.docconnect.security.oauth2.handlers.OAuth2FailureHandler;
import com.parunev.docconnect.security.oauth2.handlers.OAuth2SuccessHandler;
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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final ObjectMapper objectMapper;
    private final DCLogger dcLogger = new DCLogger(SecurityConfig.class);

    @Bean
    public HttpCookieRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieRequestRepository();
    }

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
                            PreAuthorize preAuthorize = null;

                            Object bestMatchingHandler = request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
                            if (bestMatchingHandler instanceof HandlerMethod) {
                                method = (HandlerMethod) bestMatchingHandler;
                                preAuthorize = method.getMethodAnnotation(PreAuthorize.class);
                            }

                            String message = preAuthorize == null ?
                                    authException.getMessage() :
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
                // localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:8080/oauth2/redirect
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorize")
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                        .redirectionEndpoint(red -> red.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/v1/countries/**").permitAll()
                                .requestMatchers("/api/v1/specialties/**").permitAll()
                                .requestMatchers("/api/v1/cities/**").permitAll()
                                .requestMatchers("/api/v1/auth/**" ,"/auth/**", "/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/specialist/**").permitAll()
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
