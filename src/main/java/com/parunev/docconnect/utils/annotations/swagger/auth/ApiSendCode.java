package com.parunev.docconnect.utils.annotations.swagger.auth;

import com.parunev.docconnect.models.payloads.user.login.VerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Send Custom 2FA verification code to user's email",
        description = "POST endpoint to send Custom 2FA verification code to user's email")
@ApiResponse(responseCode = "200", description = "Verification code sent successfully. Please check your email for the code")
@ApiResponse(responseCode = "400", description = "User not found.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VerificationResponse.class))})
@ApiResponse(responseCode = "401", description = "The token has expired or is invalid",content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VerificationResponse.class))})
@ApiResponse(responseCode = "401", description = "Failed to send OTP via email.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VerificationResponse.class))})
@ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VerificationResponse.class))})
public @interface ApiSendCode {
}
