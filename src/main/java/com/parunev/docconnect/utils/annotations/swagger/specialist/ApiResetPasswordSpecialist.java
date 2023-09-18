package com.parunev.docconnect.utils.annotations.swagger.specialist;

import com.parunev.docconnect.models.payloads.user.login.ForgotPasswordResponse;
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
        summary = "Reset the password in the database",
        description = "POST endpoint to change the password")
@ApiResponse(
        responseCode = "200",
        description = "Password successfully reset",
        content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ForgotPasswordResponse.class))
        })
@ApiResponse(
        responseCode = "401",
        description = "Token expired or already used",
        content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ForgotPasswordResponse.class))
        })
public @interface ApiResetPasswordSpecialist {
}
