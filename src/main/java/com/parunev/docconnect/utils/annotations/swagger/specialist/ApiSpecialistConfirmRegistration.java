package com.parunev.docconnect.utils.annotations.swagger.specialist;

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
        description = "GET(ROLE_ADMIN) endpoint to validate the specialist's account.")
@ApiResponse(
        responseCode = "200"
        , description = "Return a Response with information about the specialist and the status of the registration."
        , content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))}
)
@ApiResponse(
        responseCode = "400"
        , description = "Your account has already been verified!"
        , content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))})
public @interface ApiSpecialistConfirmRegistration {
}
