package com.parunev.docconnect.utils.annotations.swagger.auth;

import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
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
@Operation(summary = "Confirm user registration via email",
description = "GET endpoint to validate the user's email when they verify through the email confirmation")
@ApiResponse(
        responseCode = "200"
        , description = "Return a new pair of JWT tokens. With the updated role of the user."
        , content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))}
)
@ApiResponse(
        responseCode = "400"
        , description = "Email already confirmed. Your account has already been verified!"
        , content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))})
@ApiResponse(
        responseCode = "401"
        , description = "The token has expired or is invalid"
        ,content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))})
public @interface ApiConfirmRegistration {
}
