package com.parunev.docconnect.utils.annotations.swagger.specialist;

import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
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
@Operation(summary = "Verify Custom or Google 2FA verification code",
        description = "POST endpoint to verify Custom or Google 2FA verification code")
@ApiResponse(responseCode = "200", description = "Verification code verified successfully",
        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))})
@ApiResponse(responseCode = "400", description = "Specialist not found.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))})
@ApiResponse(responseCode = "401", description = "The token has expired or is invalid", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))})
public @interface ApiVerifySpecialist {
}
