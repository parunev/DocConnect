package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.user.login.LoginRequest;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.login.VerificationRequest;
import com.parunev.docconnect.models.payloads.user.login.VerificationResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationRequest;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.services.AuthService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.auth.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "CRUD operations related to authentication")
public class AuthController {

    private final AuthService authService;
    private final DCLogger dcLogger = new DCLogger(AuthController.class);

    @ApiRegister
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(
            @Parameter(description = "Payload for registration")
            @RequestBody RegistrationRequest request) {
        dcLogger.info("Registration request received for user: {}"
                , request.getEmailAddress(), request.getFirstName() + " " + request.getLastName());
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @ApiLogin
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Payload for login")
            @RequestBody LoginRequest request) {
        dcLogger.info("Login request received for user: {}", request.getEmailAddress());
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @ApiSendCode
    @PostMapping("/login/send-code")
    public ResponseEntity<VerificationResponse> sendCode(
            @Parameter(description = "Payload for sending verification code")
            @RequestBody VerificationRequest verificationRequest){
        return new ResponseEntity<>(authService.sendVerificationCode(verificationRequest), HttpStatus.OK);
    }

    @ApiVerifyCode
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponse> verifyLogin(
            @Parameter(description = "Payload for verifying login")
            @RequestBody VerificationRequest request) {
        dcLogger.info("Login verification request received for user: {}", request.getEmail());
        return new ResponseEntity<>(authService.verifyLogin(request), HttpStatus.OK);
    }

    @ApiLogout
    @PostMapping("/logout")
    public void noLogoutEndpoint() {
        throw new UnsupportedOperationException();
    }

    @ApiConfirmRegistration
    @GetMapping("/register/confirm")
    public ResponseEntity<RegistrationResponse> confirmRegister(
            @Parameter(description = "The registration token received via email.", required = true)
            @RequestParam("token") String token){
        dcLogger.info("Email confirmation request received for token: {}", token);
        return new ResponseEntity<>(authService.confirmToken(token), HttpStatus.OK);
    }
}
