package com.parunev.docconnect.utils.annotations.swagger.auth;

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
        summary = "Reset forgotten password, by receiving an email",
        description = "POST endpoint to send an email to the user with a one-time password reset url")
@ApiResponse(
        responseCode = "200",
        description = "Email sent!",
        content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ForgotPasswordResponse.class))
        })
public @interface ApiForgotPassword {
}
