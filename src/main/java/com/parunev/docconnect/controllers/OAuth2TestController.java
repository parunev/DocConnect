package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Hidden // Hidden from API documentation
@RestController
@RequiredArgsConstructor
public class OAuth2TestController {
    private final JwtTokenRepository jwtTokenRepository;

    @Operation(summary = "Test OAuth2 Redirect Endpoint", description = "For testing purposes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/oauth2/redirect")
    public ResponseEntity<LoginResponse> test(
            @RequestParam(name = "accessToken", required = false) String accessToken,
            @RequestParam(name = "refreshToken", required = false) String refreshToken
    ){
        JwtToken jwtToken = jwtTokenRepository.findByToken(accessToken).orElse(null);

        return ResponseEntity.ok(LoginResponse.builder()
                .message("OAuth2 Registration or Login success")
                .emailAddress(jwtToken.getUser().getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
