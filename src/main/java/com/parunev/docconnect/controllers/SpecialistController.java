package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.specialist.SpecialistRegistrationRequest;
import com.parunev.docconnect.models.payloads.user.login.*;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.services.SpecialistService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.specialist.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/specialist")
@Tag(name = "Specialist Controller", description = "CRUD operations related to specialists")
public class SpecialistController {

    private final SpecialistService specialistService;
    private final DCLogger dcLogger = new DCLogger(SpecialistController.class);

    @ApiSpecialistRegister
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerSpecialist(
            @Parameter(description = "Payload for registration")
            @RequestBody SpecialistRegistrationRequest request) {
        dcLogger.info("Registration request received for specialist: {}"
                , request.getEmail(), request.getFirstName() + " " + request.getLastName());

        return new ResponseEntity<>(specialistService.registerSpecialist(request), HttpStatus.CREATED);
    }

    @ApiSpecialistLogin
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginSpecialist(
            @Parameter(description = "Payload for login")
            @RequestBody LoginRequest request) {
        dcLogger.info("Login request received for specialist: {}",request.getEmailAddress());

        return new ResponseEntity<>(specialistService.specialistLogin(request), HttpStatus.OK);
    }

    @ApiSpecialistConfirmRegistration
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/register/confirm/{specialistId}")
    public ResponseEntity<VerificationResponse> confirmSpecialistRegistration(
            @Parameter(description = "Specialist id")
            @PathVariable Long specialistId) {
        dcLogger.info("Confirm registration request received for specialist with id: {}", specialistId);

        return new ResponseEntity<>(specialistService.confirmSpecialistRegistration(specialistId), HttpStatus.OK);
    }

    @ApiVerifySpecialist
    @PostMapping("/login/verify")
    public ResponseEntity<LoginResponse> verifySpecialist(
            @Parameter(description = "Payload for verification")
            @RequestBody VerificationRequest request) {
        dcLogger.info("Verification request received for specialist: {}", request.getEmail());

        return new ResponseEntity<>(specialistService.verifySpecialistLogin(request), HttpStatus.OK);
    }

    @ApiSendCodeSpecialist
    @PostMapping("/login/send-code")
    public ResponseEntity<VerificationResponse> sendCodeSpecialist(
            @Parameter(description = "Payload for sending code")
            @RequestBody VerificationRequest request) {
        dcLogger.info("Send code request received for specialist: {}", request.getEmail());

        return new ResponseEntity<>(specialistService.sendVerificationCodeToSpecialist(request), HttpStatus.OK);
    }

    @ApiForgotPasswordSpecialist
    @PostMapping("/login/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPasswordSpecialist(
            @Parameter(description = "Payload for forgot password")
            @RequestBody ForgotPasswordRequest request) {
        dcLogger.info("Forgot password request received for specialist: {}", request.getEmailAddress());
        return new ResponseEntity<>(specialistService.sendSpecialistForgotPasswordEmail(request), HttpStatus.OK);
    }

    @ApiResetPasswordSpecialist
    @PostMapping("/login/reset-password")
    public ResponseEntity<ForgotPasswordResponse> resetPasswordSpecialist(
            @Parameter(description = "Payload for resetting password")
            @RequestBody ResetPasswordRequest request) {
        dcLogger.info("Reset password request received for specialist with token: {}", request.getToken());
        return new ResponseEntity<>(specialistService.resetSpecialistPassword(request), HttpStatus.OK);
    }
}
