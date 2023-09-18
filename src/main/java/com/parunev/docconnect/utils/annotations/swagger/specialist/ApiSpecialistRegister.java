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
@Operation(
        description = "POST endpoint to sign up specialist to the application. After successful completion, the specialists" +
                "account is created and they need to wait for an admin to confirm their account.",
        summary = "Registers a new specialist to the application",
        responses = {
                @ApiResponse(
                        description = "Success - Specialist was registered",
                        responseCode = "200",
                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))}
                ),
                @ApiResponse(
                        description = "Bad Request - An invalid input was found",
                        responseCode = "400",
                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationResponse.class))}
                )
        }
)
public @interface ApiSpecialistRegister {
}
